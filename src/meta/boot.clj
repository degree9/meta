(ns meta.boot
  {:boot/export-tasks true}
  (:require [boot.core :as boot]
            [boot.util :as util]
            [boot.task.built-in :as task]
            [meta.boot.impl :as impl]
            [meta.boot.templates :as tmpl]
            [adzerk.boot-cljs :as cljs]
            [degree9.boot-nodejs :as nj]
            [degree9.boot-semgit :as sg]
            [degree9.boot-semgit.workflow :as wf]
            [degree9.boot-semver :as ver]
            [degree9.boot-welcome :as welcome]
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
  (comp (fs/feathers)
        ))

(def deps dependencies)

(boot/deftask setup
  "Setup cross project builds."
  []
  (comp (ver/version
          :develop true
          :pre-release 'snapshot)
        (dependencies)))

(boot/deftask build
  "Build project namespaces."
  []
  (comp (impl/project-files :namespaces gen-ns)
        (tmpl/project-templates :namespaces gen-ns)))

(boot/deftask client
  "Build project client."
  []
  (comp (hl/hoplon)
        ))

(boot/deftask server
  "Build project server."
  []
  (comp (nj/nodejs :init-fn 'app.server/init)
        (cljs/cljs)
        ))

(boot/deftask teardown
  "Teardown cross project builds."
  []
  (comp (task/target))

(boot/deftask develop
  "Build entire project for local development."
  []
  (comp (task/watch)
        (client)
        (server)
        (teardown)))

(def dev develop)

(boot/deftask clojars
  "Deploy project to clojars."
  []
  identity)

(boot/deftask circle
  "Preload dependencies for Circle CI."
  []
  (util/info "Hello from Circle CI!")
  identity)

(boot/deftask tests
  "Run project tests."
  []
  (util/info "Running Tests...")
  identity)

(boot/deftask initialize
  "Initialize [meta]."
  [e env      VAL str  "EDN file containing default environment settings. (env.boot)"
   s settings VAL [kw] "A list of environment keys to load from files. ([:dependencies :checkouts])"
   t tasks    VAL str  "EDN file containing a list of namespaces and tasks to require. (tasks.boot)"]
  (impl/initialize-impl *opts*))

(boot/deftask project
  "Load [meta] project."
  [p project VAL sym  "Current project name. (app)"
   n namespaces  VAL [sym]  "Project app namespaces. ([app.client app.server app.services app.routing])"]
  (let [name    (:project *opts* 'app)
        gen-ns  (:namespaces *opts*  ['app.client 'app.server 'app.services 'app.routing])
        msg     (if (and name (not= 'app name)) (str name) "Welcome!")
        dev     (:develop *opts* false)]
    (boot/set-env! :project name)
    (cond
      dev (comp (welcome/welcome :message msg)
                (wf/sync-repo)
                (setup)
                (task/watch)
                (build)
                )
      ))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
