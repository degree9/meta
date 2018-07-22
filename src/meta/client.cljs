(ns meta.client
  (:refer-clojure :exclude [find get update remove])
  (:require ["socket.io-client" :as io]
            [goog.object :as obj]
            [javelin.core :as j]
            [feathers.client :as feathers]
            ["@feathersjs/client" :as client]
            [feathers.client.services :as svc]
            [meta.promise :as prom]))

;; Feathers Client ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def app client)

(defn with-socketio [app]
  (feathers/socketio app
    (io)))

(defn with-authentication [app]
  (feathers/authentication app
    #js{:storage (obj/get js/window "localStorage")}))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Client Auth API ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn signup! [users email password]
   (svc/create users #js{:email email :password password}))

(defn login! [app strategy email password & [opts]]
  (feathers/authenticate app
    (clj->js (merge opts {:strategy strategy :email email :password password}))))

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
