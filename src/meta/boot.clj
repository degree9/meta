(ns meta.boot
  {:boot/export-tasks true}
  (:refer-clojure :exclude [compile])
  (:require [boot.core :as boot]
            [boot.util :as util]
            [boot.new :as new]
            [boot.task.built-in :as task]
            [meta.boot.init :as init]
            [meta.boot.impl :as impl]
            [meta.boot.templates :as tmpl]
            [degree9.boot-npm :as npm]
            [degree9.boot-nodejs :as njs]
            [degree9.boot-semgit :as sg]
            [degree9.boot-semgit.workflow :refer [sync-repo]]
            [degree9.boot-semver :as ver]
            [degree9.boot-shadow :as shadow]
            [degree9.boot-welcome :refer [welcome]]
            [feathers.boot-feathers :as fs]
            [hoplon.boot-hoplon :as hl]))

;; Meta Boot Tasks ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(boot/deftask proto
  "Configure [meta] for Proto-REPL."
  []
  (impl/proto-impl *opts*))

(boot/deftask dependencies
  "Download additional project dependencies."
  []
  (comp ;(fs/feathers)
        (npm/node-modules)))

(def deps dependencies)

(boot/deftask setup
  "Setup cross project builds."
  []
  (comp (ver/version)
        (dependencies)))
        ;(shadow/server)))

(boot/deftask client
  "Build project client."
  [d develop bool "Development mode will compile with optomizations `:none`."]
  (cond-> (hl/hoplon)
    (:develop *opts*)       (comp (shadow/compile :build :client))
    (not (:develop *opts*)) (comp (shadow/release :build :client))))

(boot/deftask server
  "Build project server."
  [d develop bool "Development mode will compile with optomizations `:none`."]
  (cond-> identity
    (:develop *opts*)       (comp (shadow/compile :build :server))
    (not (:develop *opts*)) (comp (shadow/release :build :server))))

(boot/deftask teardown
  "Teardown cross project builds."
  []
  (comp identity))
        ;(task/sift :include #{#"node_modules\/(.*)\.html"} :invert true)
        ;(hl/prerender)

(boot/deftask package
  "Package project builds."
  []
  (comp (task/target)))

(boot/deftask templates
  "Load project template files."
  []
  (comp
    (impl/project-files)
    (tmpl/project-templates)))

(boot/deftask clojars
  "Deploy project to clojars."
  []
  (util/warn "TODO: Clojars! \n")
  identity)

(boot/deftask circle
  "Preload dependencies for Circle CI."
  []
  (util/warn "TODO: Circle CI! \n")
  identity)

(boot/deftask cloudbuilder
  "Preload dependencies for Circle CI."
  []
  (util/info "Building for Google Cloud Builder (pre-loaded npm dependencies)! \n")
  (comp (ver/version)
        (npm/node-modules)
        (templates)
        (client)
        (server)
        (teardown)))

(boot/deftask tests
  "Run project tests."
  []
  (util/warn "TODO: Running Tests...")
  identity)

(boot/deftask compile
  "Compile project for release."
  []
  (boot/task-options!
    impl/info  {:message "Running Workflow...: compile"}
    njs/nodejs {:init-fn 'app.server/init})
  (comp
    (impl/info)
    (setup)
    (client)
    (server)
    (teardown)))

(boot/deftask develop
  "Start development workflow."
  []
  (boot/task-options!
    impl/info   {:message "Running Workflow...: develop"}
    ver/version {:develop true :pre-release 'degree9.boot-semver/snapshot}
    server      {:develop true}
    client      {:develop true}
    njs/nodejs  {:init-fn 'app.server/init})
  (comp
    (impl/info)
    (sync-repo)
    (setup)
    (task/watch)
    (client)
    (server)
    (njs/serve)
    (teardown)))

(def dev develop)

(boot/deftask nobackend
  "Start a nobackend workflow."
  []
  (boot/task-options!
    impl/info {:message "Running Workflow...: nobackend"}
    ver/version {:develop true :pre-release 'degree9.boot-semver/snapshot}
    client      {:develop true})
  (comp
    (impl/info)
    (sync-repo)
    (setup)
    (task/watch)
    (client)
    (teardown)))

(boot/deftask microservice
  "Start a microservice workflow."
  []
  (boot/task-options!
    impl/info  {:message "Running Workflow...: microservice"}
    ver/version {:develop true :pre-release 'degree9.boot-semver/snapshot}
    server      {:develop true}
    njs/nodejs  {:init-fn 'app.server/init})
  (comp
    (impl/info)
    (sync-repo)
    (setup)
    (task/watch)
    (server)
    (njs/serve)
    (teardown)))

(boot/deftask generate
  "Generate a new [meta] project."
  []
  (boot/task-options!
    impl/info {:message "Running Workflow...: generate"}
    new/new   {:template "meta" :name (str name)})
  (comp
    (impl/info)
    (new/new)))

(defn initialize
  "Initialize [meta]."
  [& opts]
  (boot/task-options!
    impl/info {:message "Running Workflow...: default"})
  (init/initialize-impl opts))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
