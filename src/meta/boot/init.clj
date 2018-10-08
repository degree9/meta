(ns meta.boot.init
  (:require [boot.core :as boot]
            [boot.util :as util]
            [meta.boot.util :as mutil]
            [clojure.java.io :as io]))

(defn initialize-env
  "Load project environment."
  [file]
  (let [menv (mutil/read-resource "meta/boot/environment.edn")
        penv (mutil/read-file file)]
    (util/info "Loading project environment...\n")
    (doseq [[key val] (merge menv penv)]
      (if (mutil/verify-env key val)
        (boot/merge-env! key val)
        (util/fail "• %s validation failed...: %s\n" (name key) val))))
  identity)

(defn initialize-settings
  "Load project settings."
  [env]
  (let []
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

(defn initialize-tasks
  "Load project tasks."
  [file]
  (let [mtask (mutil/read-resource "meta/boot/tasks.edn")
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
   (let [name     (:project opts 'app)
         env      (:env opts "./env.boot")
         settings (:settings opts [:dependencies])
         tasks    (:tasks opts "./tasks.boot")
         msg      (if (and name (not= 'app name)) (str name) "Welcome!")]
     (boot/set-env!
       :meta {:project name})
     ;; convert from task; (welcome :message msg)
     (initialize-env env)
     ;(initialize-settings settings)
     (initialize-tasks tasks))))
