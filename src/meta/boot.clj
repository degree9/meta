(ns meta.boot
  {:boot/export-tasks true}
  (:require [boot.core :as boot]
            [boot.util :as util]
            [boot.task.built-in :as task]
            [meta.boot.impl :as impl]
            [meta.boot.templates :as tmpl]
            [adzerk.boot-cljs :as cljs]
            [degree9.boot-nodejs :as njs]
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
  (comp (ver/version)
        (dependencies)))

(boot/deftask build
  "Build project namespaces."
  []
  (comp (impl/project-files)
        (tmpl/project-templates)))

(boot/deftask client
  "Build project client."
  []
  (comp (hl/hoplon)
        ))

(boot/deftask server
  "Build project server."
  []
  (comp (njs/nodejs)
        (cljs/cljs)
        ))

(boot/deftask teardown
  "Teardown cross project builds."
  []
  (comp (task/target)))

(boot/deftask develop
  "Build entire project for local development."
  []
  (comp (task/watch)
        (build)
        (client)
        (server)
        (njs/serve)
        (teardown)
        ))

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

(defn current-workflow [{:keys [develop release]}]
  (let [not-true? (fn [& args] (not-any? true? args))
        release (not-true? develop)
        develop (not-true? release)
        default (not-true? develop release)]
    {:release release :develop develop :default default}))

(boot/deftask project
  "Load [meta] project."
  [p project     VAL sym   "Current project name. (app)"
   n namespaces  VAL [sym] "Project app namespaces. ([app.client ...])"
   d develop         bool  "Project development workflow."
   r release         bool  "Project release workflow."]
  (let [name    (:project *opts* 'app)
        gen-ns  (:namespaces *opts* '[app.client app.index app.server app.services app.routing])
        msg     (if (and name (not= 'app name)) (str name) "Welcome!")
        {:keys [develop release default]} (current-workflow *opts*)]
    (boot/set-env! :project name)
    (boot/task-options!
      impl/project-files     {:namespaces gen-ns}
      tmpl/project-templates {:namespaces gen-ns}
      njs/nodejs             {:init-fn 'app.server/init})
    (cond
      develop (boot/task-options!
                ver/version            {:develop true :pre-release 'snapshot}))
    (cond-> (welcome/welcome :message msg)
      develop     (comp (wf/sync-repo)
                        (setup)
                        (meta.boot/develop))
      release     (comp (setup)
                        (build)
                        (client)
                        (server)
                        (teardown))
      default     (comp (setup)
                        (build)))))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
