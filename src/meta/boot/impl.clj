(ns meta.boot.impl
  (:require [boot.core :as boot]
            [boot.util :as util]
            [boot.task.built-in :as task]
            [clojure.java.io :as io]))

;; Meta Task Utils ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn welcome  []
  (util/info (str #"      ___           ___           ___          ___      " "\n"))
  (util/info (str #"     /\  \         /\__\         /\__\        /\  \     " "\n"))
  (util/info (str #"    |::\  \       /:/ _/_       /:/  /       /::\  \    " "\n"))
  (util/info (str #"    |:|:\  \     /:/ /\__\     /:/__/       /:/\:\  \   " "\n"))
  (util/info (str #"  __|:|\:\  \   /:/ /:/ _/_   /::\  \      /:/ /::\  \  " "\n"))
  (util/info (str #" /::::|_\:\__\ /:/_/:/ /\__\ /:/\:\  \    /:/_/:/\:\__\ " "\n"))
  (util/info (str #" \:\--\  \/__/ \:\/:/ /:/  / \/__\:\  \   \:\/:/  \/__/ " "\n"))
  (util/info (str #"  \:\  \        \::/_/:/  /       \:\  \   \::/__/      " "\n"))
  (util/info (str #"   \:\  \        \:\/:/  /         \:\  \   \:\  \      " "\n"))
  (util/info (str #"    \:\__\        \::/  /           \:\__\   \:\__\     " "\n"))
  (util/info (str #"     \/__/         \/__/             \/__/    \/__/     " "\n"))
  (util/info "\n"))

(defn read-file [file]
  (when (.exists (io/file file))
    (read-string (slurp file))))

(defn read-resource [file]
  (read-file (io/resource file)))

(defn project-env []
  (merge
    (read-resource "meta/environment.edn")
    (read-file "./env.boot")))

(defn merge-env! [key val]
  (case key
    :dependencies (boot/set-env! key #(into % val))
    (boot/set-env! key val)))

(defn verify-env [key val]
  (case key
    :dependencies (vector? val)
    true))

(defn load-env []
  (util/info "Loading environment...\n")
  (doseq [[key val] (project-env)]
    (when (verify-env key val)
      (boot/set-env! key val))))

(defn load-project []
  (util/info "Loading project settings...\n")
  (doseq [[key val] (boot/get-env)
    :let [conf (-> key name str)
          contents (read-file (str "./" conf ".boot"))]]
    (when contents
      (util/info "• %s\n" conf)
      (merge-env! key contents))))

(defn task-list []
  (->> (conj
         (read-resource "meta/tasks.edn")
         (read-file "./tasks.boot"))
       (remove nil?)))

(defn load-tasks []
  (util/info "Loading tasks...\n")
  (doseq [[n _ t :as req] (task-list)]
    (util/info "• %s from %s...\n" t n)
    (require req)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Meta Task Impl ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn init-impl []
  (welcome)
  (load-env)
  (load-project)
  (load-tasks))

(defn proto-impl []
  (util/info "Configuring Proto-REPL... \n")
  (boot/set-env! :dependencies #(into % '[[org.clojure/tools.namespace "0.2.11" :scope "test"]]))
  (require 'clojure.tools.namespace.repl)
  (eval '(apply clojure.tools.namespace.repl/set-refresh-dirs (boot/get-env :directories))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
