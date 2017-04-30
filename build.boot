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
  (comp
    (version :develop true :minor 'inc :pre-release 'snapshot)
    (watch)
    (build-jar)))

;; set project options
(task-options!
  pom {:project 'degree9/meta})
