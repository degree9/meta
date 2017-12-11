(ns meta.boot.util
  (:require [boot.core :as boot]
            [boot.util :as util]
            [clojure.java.io :as io]))

;; Meta Task Utils ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn meta-banner  []
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
  (read-string (slurp (io/resource file))))

(defn validate-checkouts [chk]
  (prn chk)
  (vector? chk))

(defn validate-folders [folders]
  (empty? (remove true? (map #(.exists (io/file "./" %)) folders))))

(defn verify-env [key val]
  (case key
    :asset-paths    (validate-folders val)
    :source-paths   (validate-folders val)
    :resource-paths (validate-folders val)
    :dependencies   (vector? val)
    :checkouts      (validate-checkouts val)
    true))

(defn ns->path [n & [ext]]
  (str (clojure.string/join "/"
        (clojure.string/split
          (clojure.string/replace (str n) #"-" "_") #"\.")) ext))

(defn spit-file [dir file content]
  (let [outf (io/file dir file)]
    (util/info "• %s\n" file)
    (doto outf io/make-parents (spit content))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
