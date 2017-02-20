(ns meta.boot
  {:boot/export-tasks true}
  (:require [boot.core :as boot]
            [meta.boot.util :as util]
            [boot.pod :as pod]
            [boot.file :as file]
            [clojure.java.io :as io]
            [clojure.string :as s]))

;; Meta Boot Utils ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Files
(defn read-file [file]
  (when (.exists (io/file file))
    (util/info-item file)
    (read-string (slurp file))))

;; Environment
(defn read-env []
  (read-file "./env.boot"))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Meta Boot Internal API ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn- get-env []
  (merge (boot/get-env) (read-env)))

(defn- load-defaults []
  (let [env (get-env)]
    (doseq [[key val] env]
      (let [conf (-> key name str)
            contents (read-file (str "./" conf ".boot"))]
        (when contents
          (util/info (str "Loading " (s/capitalize conf) "..."))
          (boot/set-env! key contents))))))

(defn- require-defaults []
  (require '[adzerk.bootlaces :refer :all]
           '[degree9.boot-semver :refer :all]
           '[degree9.boot-semgit :refer :all]
           '[degree9.boot-semgit.workflow :refer :all]))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;; Meta Boot Public API ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn init! []
  (util/info "Initializing...")
  (load-defaults)
  (require-defaults))
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
