;; Add src to classpath
(set-env! :resource-paths #{"src"})

;; Load from classpath
(require '[meta.boot.impl :as impl])

;; initialize internally
(impl/init-impl)

;; Load public tasks
(require '[meta.boot :refer [proto]])

;; project tasks
(deftask develop
  ""
  []
  identity)

;; set project options
(task-options!
  pom {:project 'degree9/meta
       :version "0.0.0"})
