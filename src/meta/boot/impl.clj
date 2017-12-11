(ns meta.boot.impl
  (:require [boot.core :as boot]
            [boot.util :as util]
            [boot.pod  :as pod]
            [boot.task.built-in :as task]
            [meta.boot.util :as mutil]
            [clojure.java.io :as io]))

;; Meta Task Impl ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(boot/deftask initialize-welcome
  "Load [meta] banner."
  []
  (mutil/meta-banner)
  identity)

(boot/deftask initialize-env
  "Load project environment."
  [f file VAL str "EDN file containing default environment settings."]
  (let [file (:file *opts*)
        menv (mutil/read-resource "meta/boot/environment.edn")
        penv (mutil/read-file file)]
    (util/info "Loading project environment...\n")
    (doseq [[key val] (merge menv penv)]
      (if (mutil/verify-env key val)
        (boot/merge-env! key val)
        (util/fail "• %s validation failed...: %s\n" (name key) val))))
  identity)

(boot/deftask initialize-settings
  "Load project settings."
  [e env VAL [kw] "List of environment keys to load from files."]
  (let [env (:env *opts*)]
    (util/info "Loading project settings...\n")
    (doseq [key env
            :let [conf (-> key name str)
                  contents (mutil/read-file (str "./" conf ".boot"))]]
      (when contents
        (util/info "• %s\n" conf)
        (if (mutil/verify-env key contents)
          (boot/merge-env! key contents)
          (util/fail "• %s validation failed...: %s\n" (name key) contents)))))
  identity)

(boot/deftask initialize-tasks
  "Load project tasks."
  [f file VAL str "EDN file containing a list of namespaces and tasks to require."]
  (let [file  (:file *opts*)
        mtask (mutil/read-resource "meta/boot/tasks.edn")
        ptask (mutil/read-file file)
        tasks (conj mtask ptask)]
    (util/info "Loading project tasks...\n")
    (doseq [[n _ r :as req] (remove nil? tasks)]
      (cond (keyword? r) (util/info "• %s from %s\n" r n)
            (vector? r)  (doseq [t r] (util/info "• %s from %s\n" t n))
            :else        (util/fail "Failed to Load Tasks...: %s \n" req))
      (require req)))
  identity)

(defn initialize-impl
  ([] (initialize-impl {}))
  ([opts]
   (let [env      (:env opts "./env.boot")
         settings (:settings opts [:dependencies])
         tasks    (:tasks opts "./tasks.boot")]
     (boot/set-env! :meta {})
     (comp
       (initialize-welcome)
       (initialize-env :file env)
       (initialize-settings :env settings)
       (initialize-tasks :file tasks)))))

(boot/deftask project-files
  "Load project files."
  [n namespaces VAL [sym] "Project namespaces to validate."]
  (boot/with-pre-wrap fs
    (let [gen-ns   (:namespaces *opts*)
          paths    (map mutil/ns->path gen-ns)
          tmp      (boot/tmp-dir!)
          in-files (boot/input-files fs)]
      (util/info "Loading project files...\n")
      (doseq [path paths]
        (let [cljs-path (format "%s.cljs" path)
              tmpl-path (format "%s.mustache" path)]
          ;; search for project cljs file
          (if-let [cljs-file (->> in-files (boot/by-path [cljs-path]) first)]
            (util/dbug "• %s... skipping!\n" tmpl-path)
            ;; search for project template file
            (if-let [tmpl-file (->> in-files (boot/by-path [tmpl-path]) first)]
              (util/info "• %s\n" tmpl-path)
              ;; search for default template file
              (if-let [tmpl-file (io/resource (format "meta/%s.mustache" path))]
                (mutil/spit-file tmp tmpl-path (slurp tmpl-file))
                ;; warn missing project and default template
                (util/warn "• %s... missing!\n" tmpl-path))))))
      (-> fs (boot/add-resource tmp) boot/commit!))))

(defn proto-impl [_]
  (util/info "Configuring Proto-REPL... \n")
  (boot/set-env! :dependencies #(into % '[[org.clojure/tools.namespace "0.2.11" :scope "test"]]))
  (require 'clojure.tools.namespace.repl)
  (eval '(apply clojure.tools.namespace.repl/set-refresh-dirs (boot.core/get-env :directories)))
  identity)

(boot/deftask info
  "A task which displays a message."
  [m message VAL str "Message to be displayed."]
  (boot/with-pre-wrap fs
    (let []
      (util/info (str (:message *opts*) "\n"))
      fs)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
