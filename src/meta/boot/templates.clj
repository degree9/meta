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
              cljs-path (format "%s.cljs" path)]
          (if-let [edn-file (->> files (boot/by-ext [".edn"]) first)]
            (if-let [cljs-file (->> files (boot/by-ext [".cljs"]) first)]
              (let [outf    (boot/tmp-path cljs-file)
                    instr   (slurp (boot/tmp-file cljs-file))
                    data    (slurp (boot/tmp-file edn-file))]
                (mutil/spit-file tmp outf (stencil/render-string instr data)))
              (util/warn "• %s... missing!\n" cljs-path))
            (when-let [edn-file (io/resource (format "meta/%s.edn" path))]
              (if-let [cljs-file (->> files (boot/by-ext [".cljs"]) first)]
                (let [outf   (boot/tmp-path cljs-file)
                      instr  (slurp (boot/tmp-file cljs-file))
                      data   (read-string (slurp edn-file))]
                  (mutil/spit-file tmp outf (stencil/render-string instr data)))
                (util/warn "• %s... missing!\n" cljs-path))))))
      (-> fs (boot/add-resource tmp) boot/commit!))))
