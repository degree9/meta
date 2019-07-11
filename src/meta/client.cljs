(ns meta.client
  (:refer-clojure :exclude [find get update remove])
  (:require ["socket.io-client" :as io]
            ["@feathersjs/client" :as client]
            ["jquery" :as jquery]
            [goog.object :as obj]
            [feathers.client :as feathers]
            [feathers.client.services :as svc]
            [meta.promise :as prom]))

;; Feathers Client ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def app client)

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
(defn signup! [users email password]
   (svc/create users #js{:email email :password password}))

(defn login! [app strategy & [opts]]
  (feathers/authenticate app
    (clj->js (merge opts {:strategy strategy}))))

(defn logout! [app]
  (feathers/logout app))

(defn auth! [app]
  (feathers/authenticate app))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Client Service API ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def service feathers/service)

(def find    svc/find)

(def get     svc/get)

(def create  svc/create)

(def update  svc/update)

(def patch   svc/patch)

(def remove  svc/remove)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Client Event API ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def on      svc/on)

(def created svc/created)

(def updated svc/updated)

(def patched svc/patched)

(def removed svc/removed)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
