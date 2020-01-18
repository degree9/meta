;; Add src/resources to classpath
(set-env! :resource-paths #{"src" "resources"}
          :dependencies '[;[org.clojure/clojure         "1.10.0"]
                          ;[org.clojure/clojurescript   "1.10.520"]
                          ;[org.clojure/tools.namespace "0.3.0-alpha4"]
                          ;[org.clojure/tools.reader    "1.3.2"]
                          ;[org.clojure/test.check      "0.10.0-alpha3"]
                          [stencil                     "0.5.0" :exclusions [org.clojure/clojure org.clojure/core.cache]]
                          ;[proto-repl                  "0.3.1"]
                          ;[adzerk/boot-reload          "0.5.1"]
                          ;[boot/core                   "2.8.2"]
                          [boot/new                    "0.5.3"]
                          ;[degree9/covenant            "0.1.0-SNAPSHOT"]
                          ;[degree9/boot-docker         "0.1.0-SNAPSHOT"]
                          [degree9/boot-nodejs         "1.3.0"]
                          [degree9/boot-npm            "1.11.0"]
                          ;[degree9/boot-exec           "1.1.0-SNAPSHOT"]
                          [degree9/boot-semver         "1.8.0"]
                          [degree9/boot-semgit         "1.3.0"]
                          [degree9/boot-shadow         "2.8.14-0"]
                          ;[degree9/boot-electron       "0.2.0"]
                          [degree9/boot-welcome        "1.0.0"]
                          ;[degree9/electron-cljs       "0.1.0"]
                          [degree9/featherscript       "0.6.0-SNAPSHOT"]
                          ;[degree9/firebase-cljs       "1.3.0"]
                          ;[degree9/nodejs-cljs         "0.1.0"]
                          [degree9/meta-template       "0.5.0-SNAPSHOT"]
                          [hoplon/hoplon               "7.3.0-SNAPSHOT"]
                          [hoplon/brew                 "7.2.0-SNAPSHOT"]])

;; Load from classpath
(require '[meta.boot.init               :as init]
         '[degree9.boot-semver          :refer :all]
         '[degree9.boot-shadow          :as shadow])

;; Initialize internally - projects should NOT do this
(init/initialize-impl)

;; Load public tasks
(require '[meta.boot :as m])

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
