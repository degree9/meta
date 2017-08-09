(ns meta.boot
  {:boot/export-tasks true}
  (:require [boot.core :as boot]
            [boot.util :as util]
            [boot.new :as new]
            [boot.task.built-in :as task]
            [meta.boot.impl :as impl]
            [meta.boot.templates :as tmpl]
            [adzerk.boot-cljs :as cljs]
            [degree9.boot-nodejs :as njs]
            [degree9.boot-semgit :as sg]
            [degree9.boot-semgit.workflow :refer [sync-repo]]
            [degree9.boot-semver :as ver]
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

(defn current-workflow [{:keys [develop release snapshot generate]}]
  (let [not-true? (fn [& args] (not-any? true? args))
        release  (and release  (not-true? develop snapshot generate))
        snapshot (and snapshot (not-true? develop release generate))
        develop  (and develop  (not-true? release snapshot generate))
        generate (and generate (not-true? release snapshot develop))
        default  (not-true? develop release snapshot generate)]
    {:release release :snapshot snapshot :develop develop :default default :generate generate}))

(boot/deftask project
  "Load [meta] project."
  [p project     VAL sym   "Current project name. (app)"
   n namespaces  VAL [sym] "Project app namespaces. ([app.client ...])"
   d develop         bool  "Project development workflow."
   r release         bool  "Project release workflow."
   s snapshot        bool  "Project snapshot workflow."
   g generate        bool  "Generate an empty project template."]
  (let [name    (:project *opts* 'app)
        gen-ns  (:namespaces *opts* '[app.client app.index app.dashboard app.server
                                      app.services app.routing app.nobackend])
        msg     (if (and name (not= 'app name)) (str name) "Welcome!")
        wfmsg   #(format "Running Workflow...: %s" %)
        {:keys [develop release snapshot default] :as workflows} (current-workflow *opts*)]
    (boot/set-env! :project name)
    (boot/task-options!
      impl/project-files     {:namespaces gen-ns}
      tmpl/project-templates {:namespaces gen-ns}
      njs/nodejs             {:init-fn 'app.server/init}
      ;cljs/cljs              {:source-map       true
      ;                        :compiler-options {:pseudo-names true
      ;                                           :pretty-print true
      ;                                           :language-in :ecmascript5
      ;                                           :parallel-build true}}
      )
    (cond
      develop  (boot/task-options!
                 impl/info              {:message "Running Workflow...: develop"}
                 ver/version            {:develop true :pre-release 'snapshot}
                 cljs/cljs              {:optimizations :none})
      snapshot (boot/task-options!
                 impl/info              {:message "Running Workflow...: snapshot"}
                 ver/version            {:develop true :pre-release 'snapshot})
      release (boot/task-options!
                 impl/info              {:message "Running Workflow...: release"})
      generate (boot/task-options!
                 impl/info              {:message "Running Workflow...: generate"}
                 new/new                {:template "meta" :name (str name)})
      default (boot/task-options!
                 impl/info              {:message "Running Workflow...: default"}))
    (cond-> (welcome :message msg)
      develop     (comp (impl/info)
                        (sync-repo)
                        (setup)
                        (meta.boot/develop))
      snapshot    (comp (impl/info)
                        (setup)
                        (build)
                        (client)
                        (server)
                        (teardown)
                        (ver/build-jar)
                        (ver/push-snapshot))
      release     (comp (impl/info)
                        (setup)
                        (build)
                        (client)
                        (server)
                        (teardown)
                        (ver/build-jar)
                        (ver/push-release))
      generate    (comp (impl/info)
                        (new/new))
      default     (comp (impl/info)
                        (setup)
                        (build)))))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
