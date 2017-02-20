(set-env! :resource-paths #{"src"})

(require '[meta.boot :as meta])

(meta/init!)

(task-options!
  pom {:project 'degree9/meta
       :version "0.0.0"})

(deftask testing
  ""
  []
  identity)
