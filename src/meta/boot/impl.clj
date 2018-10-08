(ns meta.boot.impl
  (:require [boot.core :as boot]
            [boot.util :as util]
            [boot.pod  :as pod]
            [boot.task.built-in :as task]
            [meta.boot.util :as mutil]
            [clojure.java.io :as io]))

;; Meta Task Impl ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

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
