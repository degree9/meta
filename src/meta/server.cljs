(ns meta.server
  (:require [feathers.app :as feathers]
            [meta.server.services :as services]
            ))

(enable-console-print!)

(def app (feathers/feathers))

(def public "./")

(-> app
    (feathers/configuration public)
    feathers/compress
    feathers/cors
    (feathers/favicon (str public "favicon.ico"))
    (feathers/static public)
    feathers/body-parser
    feathers/hooks
    feathers/rest
    feathers/socketio
    feathers/authentication
    services/users
    )

(defn api
  ([path svc hooks] (feathers/api app path svc hooks))
  ([app path svc hooks] (feathers/api app path svc hooks)))

(defn listen
  ([port] (feathers/listen app port))
  ([app port] (feathers/listen app port)))

(defn init! [fname]
  (set! *main-cli-fn* fname))
