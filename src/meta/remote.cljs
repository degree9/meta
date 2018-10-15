(ns meta.remote
  "Provides methods for connecting via microservices pattern."
  (:require
    ["@feathersjs/client" :as feathers]
    ["request" :as req]
    [goog.object :as obj]
    [feathers.client :as client]
    [feathers.client.services :as svc]))

(defn with-request [app uri & opts]
  (client/request app uri (.defaults req opts)))

(defn connection
  "Create a connection to a remote server."
  ([uri] (connection uri {}))
  ([uri opts] (with-request (feathers) uri opts)))

(defn service
  "Create a service which will proxy requests to a remote service."
  [service & [opts]]
  (let [uri (:uri opts)]
    (reify
      Object
      (id [this] "id")
      (setup [this app path]
        (obj/set this "remote"
          (client/service (connection uri) service)))
      (find [this params]
        (svc/find (obj/get this "remote") params))
      (get [this id params]
        (svc/get (obj/get this "remote") id params))
      (create [this data params]
        (svc/create (obj/get this "remote") data params))
      (update [this id data params]
        (svc/update (obj/get this "remote") id data params))
      (patch [this id data params]
        (svc/patch (obj/get this "remote") id data params))
      (remove [this id params]
        (svc/remove (obj/get this "remote") id params)))))
