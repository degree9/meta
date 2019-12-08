(ns meta.server
  (:require ["debug" :as dbg]
            [feathers.application :as feathers]
            [feathers.authentication :as auth]
            [feathers.configuration :as config]
            [feathers.express :as exp]
            [feathers.socketio :as socketio]
            [meta.channels :as chan]))

(def debug (dbg "degree9:meta:server"))

(enable-console-print!)

(defn app []
  (debug "Starting application server")
  (feathers/app))

(defn with-defaults [app]
  (debug "Loading server defaults")
  (-> app
    exp/express
    config/configuration
    exp/json
    exp/urlencoded
    exp/static))

(defn with-rest [app]
  (debug "Loading server REST api")
  (exp/rest app))

(defn with-socketio [app]
  (debug "Loading server SocketIO api")
  (socketio/socketio app))

(defn with-authentication [app]
  (debug "Loading server Authentication api")
  (auth/configure app))

(defn with-channels [app]
  (debug "Loading server Channels api")
  (chan/join-anonymous app))

(defn with-error-handler [app]
  (debug "Loading server Error Handler api")
  (-> app
    exp/not-found
    exp/error-handler))

(defn using [app path svc]
  (debug "Passing app.use call to feathers")
  (feathers/using app path svc))

(defn api
  [app path svc hooks]
  (debug "Passing app.api call to feathers")
  (feathers/api app path svc hooks))

(defn listen
  [app port]
  (debug "Passing app.listen call to feathers")
  (feathers/listen app port))

(defn init! [fname]
  (debug "Setting nodejs *main-cli-fn*")
  (set! *main-cli-fn* fname))
