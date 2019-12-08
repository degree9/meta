(ns meta.client
  (:refer-clojure :exclude [find get update remove])
  (:require ["socket.io-client" :as io]
            ["jquery" :as jquery]
            [goog.object :as obj]
            [feathers.client :as client]
            [feathers.client.services :as svc]))

;; Feathers Client ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def app client)

(defn with-jquery [app uri]
  (client/jquery app uri (jquery)))

(defn with-socketio [app & [uri opts]]
  (client/socketio app (io uri opts)))

(defn with-authentication
  ([app]
   (with-authentication app (obj/get js/window "localStorage")))
  ([app storage]
   (client/authentication app #js{:storage storage})))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Client Auth API ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn login! [app strategy & [opts]]
  (client/authenticate app
    (clj->js (merge opts {:strategy strategy}))))

(defn logout! [app]
  (client/logout app))

(defn auth! [app]
  (client/reauthenticate app))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Client Service API ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def service client/service)

(defn find [service & [params]]
  (client/find service (clj->js params)))

(defn get [service id & [params]]
  (client/get service id (clj->js params)))

(defn create [service data & [params]]
  (client/create service (clj->js data) (clj->js params)))

(defn update [service id data & [params]]
  (client/update service id (clj->js data) (clj->js params)))

(defn patch [service id data & [params]]
  (client/patch service id (clj->js data) (clj->js params)))

(defn remove [service id & [params]]
  (client/remove service id (clj->js params)))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Client Event API ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def on      svc/on)

(def created svc/created)

(def updated svc/updated)

(def patched svc/patched)

(def removed svc/removed)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
