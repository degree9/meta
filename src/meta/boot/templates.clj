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
      (util/info "Loading project templates...\n")
      (doseq [path paths]
        (let [files     (->> in-files (boot/by-re [(re-pattern (str "^" path))]))
              cljs-path (format "%s.cljs" path)
              tmpl-path (format "%s.mustache" path)]
          (if-let [edn-file (->> files (boot/by-ext [".edn"]) first)]
            (if-let [tmpl-file (->> files (boot/by-ext [".mustache"]) first)]
              (let [out-file cljs-path
                    tmpl-str (slurp (boot/tmp-file tmpl-file))
                    edn-data (slurp (boot/tmp-file edn-file))]
                (mutil/spit-file tmp out-file (stencil/render-string tmpl-str edn-data)))
              (util/warn "• %s... missing!\n" cljs-path))
            (when-let [edn-file (io/resource (format "meta/%s.edn" path))]
              (if-let [tmpl-file (->> files (boot/by-ext [".mustache"]) first)]
                (let [out-file cljs-path
                      tmpl-str (slurp (boot/tmp-file tmpl-file))
                      edn-data (read-string (slurp edn-file))]
                  (mutil/spit-file tmp out-file (stencil/render-string tmpl-str edn-data)))
                (util/warn "• %s... missing!\n" cljs-path))))))
      (-> fs (boot/add-resource tmp) boot/commit!))))
