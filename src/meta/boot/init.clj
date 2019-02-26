(ns meta.boot.init
  (:require [boot.core :as boot]
            [boot.util :as util]
            [meta.boot.util :as mutil]
            [clojure.java.io :as io]))

(defn- loading-msg [name]
  (util/info "Loading project %s...\n" name))

(defn- task-msg [req sym]
  (util/info "• %s from %s\n" req sym))

(defn- merge-env [env]
  (doseq [[key val] env]
    (if (mutil/verify-env key val)
      (boot/merge-env! key val)
      (util/fail "• %s validation failed...: %s\n" (name key) val))))

(defn initialize-env
  "Load project environment from file (accepts optional key)."
  [file msg & [key]]
  (when-let [env (mutil/read-file file)]
    (loading-msg msg)
    (merge-env (if key {key (get env key)} env))))

(defn initialize-tasks
  "Load project tasks from file."
  [file]
  (when-let [tasks (mutil/read-file file)]
    (loading-msg "tasks")
    (doseq [task tasks] (require task))))

(defn initialize-impl
  ([] (initialize-impl {}))
  ([opts]
   (let [name     (:project opts 'app)
         env      (:env opts "./env.boot")
         shadow   (:shadow opts "./shadow-cljs.edn")
         tasks    (:tasks opts "./tasks.boot")
         msg      (if (and name (not= 'app name)) (str name) "Welcome!")]
     (boot/set-env! :meta {:project name})
     (initialize-env env "environment")
     (initialize-env shadow "dependencies" :dependencies)
     (initialize-tasks tasks))))
