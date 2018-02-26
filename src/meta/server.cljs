(ns meta.server
  (:require [feathers.app :as feathers]
            [meta.channels :as chan]))

(enable-console-print!)

(def app (-> (feathers/feathers) feathers/express))

(defn with-defaults [app]
  (-> app
    feathers/configuration
    feathers/json
    feathers/urlencoded
    feathers/static))

(defn with-rest [app]
  (-> app
    feathers/rest))

(defn with-socketio [app]
  (-> app
    feathers/socketio))

(defn with-authentication [app]
  (-> app
    feathers/authentication))

(defn with-channels [app]
  (-> app
    chan/join-anonymous))

(defn using
  ([path svc] (using app path svc))
  ([app path svc] (feathers/using app path svc)))

(defn api
  ([path svc hooks] (api app path svc hooks))
  ([app path svc hooks] (feathers/api app path svc hooks)))

(defn listen
  ([port] (listen app port))
  ([app port] (feathers/listen app port)))

(defn init! [fname]
  (set! *main-cli-fn* fname))
