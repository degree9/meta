(ns meta.client
  (:refer-clojure :exclude [find get update remove])
  (:require ["socket.io-client" :as io]
            ["jquery" :as jquery]
            [goog.object :as obj]
            [feathers.client :as feathers]
            [feathers.client.services :as svc]))

;; Feathers Client ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def app feathers/client)

(defn with-jquery [app uri]
  (feathers/jquery app uri (jquery)))

(defn with-socketio [app & [uri opts]]
  (feathers/socketio app (io uri opts)))

(defn with-authentication
  ([app]
   (with-authentication app (obj/get js/window "localStorage")))
  ([app storage]
   (feathers/authentication app #js{:storage storage})))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Client Auth API ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn login! [app strategy & [opts]]
  (feathers/authenticate app
    (clj->js (merge opts {:strategy strategy}))))

(defn logout! [app]
  (feathers/logout app))

(defn auth! [app]
  (feathers/reauthenticate app))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Client Service API ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def service feathers/service)

(defn find [service & [params]]
  (svc/find service (clj->js params)))

(defn get [service id & [params]]
  (svc/get service id (clj->js params)))

(defn create [service data & [params]]
  (svc/create service (clj->js data) (clj->js params)))

(defn update [service id data & [params]]
  (svc/update service id (clj->js data) (clj->js params)))

(defn patch [service id data & [params]]
  (svc/patch service id (clj->js data) (clj->js params)))

(defn remove [service id & [params]]
  (svc/remove service id (clj->js params)))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Client Event API ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn on [service event callback]
  (svc/on service event (comp js->clj callback)))

(defn created [service callback]
  (svc/on service "created" (comp js->clj callback)))

(defn updated [service callback]
  (svc/on service "updated" (comp js->clj callback)))

(defn patched [service callback]
  (svc/on service "patched" (comp js->clj callback)))

(defn removed [service callback]
  (svc/on service "removed" (comp js->clj callback)))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
