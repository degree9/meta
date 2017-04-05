(ns meta.boot
  {:boot/export-tasks true}
  (:require [boot.core :as boot]
            [boot.pod :as pod]
            [boot.file :as file]
            [boot.task.built-in :as task]
            [meta.boot.util :as util]
            [clojure.java.io :as io]
            [clojure.string :as s]))

;; Meta Boot ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn welcome  []
  (util/info (str #"      ___           ___           ___          ___      "))
  (util/info (str #"     /\  \         /\__\         /\__\        /\  \     "))
  (util/info (str #"    |::\  \       /:/ _/_       /:/  /       /::\  \    "))
  (util/info (str #"    |:|:\  \     /:/ /\__\     /:/__/       /:/\:\  \   "))
  (util/info (str #"  __|:|\:\  \   /:/ /:/ _/_   /::\  \      /:/ /::\  \  "))
  (util/info (str #" /::::|_\:\__\ /:/_/:/ /\__\ /:/\:\  \    /:/_/:/\:\__\ "))
  (util/info (str #" \:\--\  \/__/ \:\/:/ /:/  / \/__\:\  \   \:\/:/  \/__/ "))
  (util/info (str #"  \:\  \        \::/_/:/  /       \:\  \   \::/__/      "))
  (util/info (str #"   \:\  \        \:\/:/  /         \:\  \   \:\  \      "))
  (util/info (str #"    \:\__\        \::/  /           \:\__\   \:\__\     "))
  (util/info (str #"     \/__/         \/__/             \/__/    \/__/     "))
  (util/info "\n"))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Meta Boot Utils ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn read-file [file]
  (when (.exists (io/file file))
    (util/info-item file)
    (read-string (slurp file))))

(defn read-env []
  (read-file "./env.boot"))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Meta Boot Internal API ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn- load-env []
  (doseq [[key val] (read-env)
    :let [conf (-> key name str)]]
    (util/info "Loading" (str conf "..."))
    (boot/set-env! key val)))

(defn- load-defaults []
  (doseq [[key val] (boot/get-env)
    :let [conf (-> key name str)
          contents (read-file (str "./" conf ".boot"))]]
    (when contents
      (util/info "Loading" (str (s/capitalize conf) "..."))
      (boot/set-env! key contents))))

(defn- require-defaults []
  (require '[adzerk.boot-cljs             :refer [cljs]]
           '[adzerk.boot-reload           :refer [reload]]
           '[adzerk.bootlaces             :refer :all]
           '[degree9.boot-exec            :refer [exec]]
           '[degree9.boot-nodejs          :refer [nodejs serve]]
           '[degree9.boot-npm             :refer [npm]]
           '[degree9.boot-semver          :refer [version]]
           '[degree9.boot-semgit          :refer :all]
           '[degree9.boot-semgit.workflow :refer :all]
           '[feathers.boot-feathers       :refer [feathers]]
           ))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Meta Boot Tasks ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(boot/deftask initialize
  "Initialize [meta]."
  []
  (welcome)
  (util/info "Initializing...")
  (load-env)
  (load-defaults)
  (require-defaults)
  identity)

(def init initialize)

(boot/deftask proto
  "Configure [meta] for Proto-REPL."
  []
  (util/info "Configuring Proto-REPL...")
  (boot/set-env! :dependencies #(into % '[[org.clojure/tools.namespace "0.2.11"]]))
  (require 'clojure.tools.namespace.repl)
  (eval '(apply clojure.tools.namespace.repl/set-refresh-dirs
                (get-env :directories)))
  identity)

(boot/deftask develop
  "Build project for local development."
  []
  (comp ;(git-pull :branch "origin/master")
        (watch)))

(def dev develop)

(boot/deftask deploy
  "Deploy project to clojars."
  []
  identity)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Meta Boot Public API ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;(defn- write-manifest!
;  [fileset dir]
;  (let [msg (delay )
;        out (io/file dir "hoplon" "manifest.edn")]
;    (when-let [hls (seq (->> fileset
;                             boot/output-files
;                             (boot/by-ext [".hl"])
;                             (map (comp fspath->jarpath boot/tmp-path))))]
;      @msg
;      (doseq [h hls] (util/info "• %s\n" h))
;      (spit (doto out io/make-parents) (pr-str (vec hls))))
;    (boot/add-resource fileset dir)))

;(boot/deftask manifest
;  "Write a DSL manifest file."
;  [d dsl      VAL str    "DSL name."
;   e ext      VAL str    "DSL file extension."
;   r refers   VAL #{sym} "Set of namespaces to include in DSL files."]
;  (assert (:dsl *opts*) "A DSL name is required.")
;  (assert (:ext *opts*) "A DSL file extension is required.")
;  (boot/with-pre-wrap fileset
;    (let [tmp     (boot/tmp-dir!)
;          dslname (:dsl *opts*)
;          ext     (:ext *opts*)
;          out     (io/file tmp dslname "manifest.edn")]
;      (when-let [dsl (->> fileset
;                          boot/output-files
;                          (boot/by-ext [ext])
;                          (mapv (comp #(->> % file/split-path (string/join "/"))
;                                      boot/tmp-path)))]
;        (util/info "Writing DSL manifest...\n")
;        (doseq [s (seq dsl)] (util/info (str "• " d "\n")))
;        (spit (doto out io/make-parents) dsl))
;      (-> fileset (boot/add-resource dir) boot/commit!))))
