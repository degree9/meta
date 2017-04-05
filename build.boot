(set-env! :resource-paths #{"src"})

(require '[meta.boot :as meta])

(meta/initialize)

(task-options!
  pom {:project 'degree9/meta
       :version "0.0.0"})

(deftask testing
  ""
  []
  (prn "in testing")
  identity)

(deftask dev
  ""
  []
  (meta/proto))
