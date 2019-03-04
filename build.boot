;; Add src/resources to classpath
(set-env! :resource-paths #{"src" "resources"}
          :dependencies '[[org.clojure/clojure         "1.9.0"]
                          [org.clojure/clojurescript   "1.10.339"]
                          [org.clojure/tools.namespace "0.3.0-alpha4"]
                          [org.clojure/test.check      "0.10.0-alpha2"]
                          [stencil                     "0.5.0" :exclusions [org.clojure/clojure org.clojure/core.cache]]
                          ;[proto-repl                  "0.3.1"]
                          ;[adzerk/boot-reload          "0.5.1"]
                          [boot/core                   "2.7.2"]
                          [boot/new                    "0.5.2"]
                          [degree9/covenant            "0.1.0-SNAPSHOT"]
                          [degree9/boot-nodejs         "1.2.1"]
                          ;[degree9/boot-docker         "0.1.0-SNAPSHOT"]
                          [degree9/boot-npm            "1.10.0-SNAPSHOT"]
                          [degree9/boot-exec           "1.1.0-SNAPSHOT"]
                          [degree9/boot-semver         "1.8.0-SNAPSHOT"]
                          [degree9/boot-semgit         "1.3.0-SNAPSHOT"]
                          [degree9/boot-shadow         "2.6.6-SNAPSHOT"]
                          ;[degree9/boot-electron       "0.2.0"]
                          [degree9/boot-welcome        "1.0.0"]
                          ;[degree9/electron-cljs       "0.1.0"]
                          [degree9/featherscript       "0.4.0-SNAPSHOT"]
                          [degree9/firebase-cljs       "1.3.0"]
                          ;[degree9/nodejs-cljs         "0.1.0"]
                          [degree9/meta-template       "0.5.0-SNAPSHOT"]
                          [hoplon/hoplon               "7.2.0"]
                          [hoplon/brew                 "7.2.0-SNAPSHOT"]])

;; Load from classpath
(require '[meta.boot.init :as init])

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
