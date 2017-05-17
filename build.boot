;; Add src/resources to classpath
(set-env! :resource-paths #{"src" "resources"})

;; Load from classpath
(require '[meta.boot.impl :as impl])

;; initialize internally - projects should NOT do this
(impl/initialize-impl)

;; Load public tasks
(require '[meta.boot :refer [project proto circle tests]])

(task-options!
  pom     {:project 'degree9/meta})

(deftask develop
  ""
  []
  (comp
    (watch)
    (build-jar)))

(deftask develop-snapshot
  ""
  []
  (comp
    (version :develop true :pre-release 'snapshot)
    (develop)
    (push-snapshot)))

(deftask deploy
  ""
  []
  (comp
    (version)
    (build-jar)
    (push-release)))
