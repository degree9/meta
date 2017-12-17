;; Add src/resources to classpath
(set-env! :resource-paths #{"src" "resources"}
          :checkouts '[[degree9/uikit-hl "3.0.0-SNAPSHOT"]
                       [degree9/featherscript "0.4.0-SNAPSHOT"]])

;; Load from classpath
(require '[meta.boot.impl :as impl])

;; Initialize internally - projects should NOT do this
(impl/initialize-impl)

;; Load public tasks
(require '[meta.boot :refer [project proto circle tests]])

(task-options!
  pom     {:project 'degree9/meta})

;; Internal Boot Tasks
(deftask develop
  ""
  []
  (comp
    (version :develop true
             :minor 'inc
             :pre-release 'snapshot)
    (watch)
    (build-jar)))

(deftask develop-snapshot
  ""
  []
  (comp
    (develop)
    (push-snapshot)))

(deftask deploy
  ""
  []
  (comp
    (version)
    (build-jar)
    (push-release)))
