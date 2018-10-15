(ns meta.server
  (:require ["debug" :as dbg]
            [feathers.app :as feathers]
            [meta.channels :as chan]))

(def debug (dbg "degree9:meta:server"))

(enable-console-print!)

(defn app []
  (debug "Starting [meta] server")
  (feathers/app))

(defn with-defaults [app]
  (debug "Loading server defaults")
  (-> app
    feathers/express
    feathers/configuration
    feathers/json
    feathers/urlencoded
    feathers/static))

(defn with-rest [app]
  (debug "Loading server REST api")
  (-> app
    feathers/rest))

(defn with-socketio [app]
  (debug "Loading server SocketIO api")
  (-> app
    feathers/socketio))

(defn with-authentication [app]
  (debug "Loading server Authentication api")
  (-> app
    feathers/authentication))

(defn with-channels [app]
  (debug "Loading server Channels api")
  (-> app
    chan/join-anonymous))

(defn using
  ([path svc]
   (using app path svc))
  ([app path svc]
   (debug "Passing app.use call to feathers")
   (.use app path svc)))

(defn api
  ([path svc hooks]
   (api app path svc hooks))
  ([app path svc hooks]
   (debug "Passing app.api call to feathers")
   (feathers/api app path svc hooks)))

(defn listen
  ([port]
   (listen app port))
  ([app port]
   (debug "Passing app.listen call to feathers")
   (.listen app port)))

(defn init! [fname]
  (debug "Setting nodejs *main-cli-fn*")
  (set! *main-cli-fn* fname))
