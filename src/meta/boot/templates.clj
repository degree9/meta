(ns meta.boot.templates
  (:require [boot.core :as boot]
            [boot.util :as util]
            [meta.boot.impl :as impl]
            [meta.boot.util :as mutil]
            [clojure.java.io :as io]
            [stencil.core :as stencil]))

(boot/deftask project-templates
  "Load project templates."
  [n namespaces VAL [sym] "Project namespaces to generate."]
  (boot/with-pre-wrap fs
    (let [gen-ns   (:namespaces *opts*)
          paths    (map mutil/ns->path gen-ns)
          tmp      (boot/tmp-dir!)
          in-files (boot/input-files fs)]
      (util/info "Compiling project files...\n")
      (doseq [path paths]
        (let [files     (->> in-files (boot/by-re [(re-pattern (str "^" path))]))
              cljs-path (format "%s.cljs" path)
              tmpl-path (format "%s.mustache" path)]
          ;; search for project edn file
          (if-let [edn-file (->> files (boot/by-ext [".edn"]) first)]
            ;; search for project template file
            (if-let [tmpl-file (->> files (boot/by-ext [".mustache"]) first)]
              ;; generate cljs from project edn and template files
              (let [out-file cljs-path
                    tmpl-str (slurp (boot/tmp-file tmpl-file))
                    edn-data (read-string (slurp (boot/tmp-file edn-file)))]
                (mutil/spit-file tmp out-file (stencil/render-string tmpl-str edn-data)))
              ;; warn missing project template file
              (util/warn "• %s... missing!\n" tmpl-path))
            ;; search for default edn file
            (when-let [edn-file (io/resource (format "meta/%s.edn" path))]
              ;; search for project template file
              (when-let [tmpl-file (->> files (boot/by-ext [".mustache"]) first)]
                ;; generate cljs from default edn and template files
                (let [out-file cljs-path
                      tmpl-str (slurp (boot/tmp-file tmpl-file))
                      edn-data (read-string (slurp edn-file))]
                  (mutil/spit-file tmp out-file (stencil/render-string tmpl-str edn-data))))))))
      (-> fs (boot/add-resource tmp) boot/commit!))))
