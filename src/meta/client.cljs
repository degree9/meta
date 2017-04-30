(ns meta.client
  (:refer-clojure :exclude [find get update remove])
  (:require [cljsjs.socket-io]
            [goog.object :as obj]
            [feathers.client :as feathers]
            [feathers.client.services :as svc]))

;; Feathers Client ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def ^:dynamic *app* (feathers/feathers))

(-> *app*
    (feathers/socketio (js/io))
    (feathers/hooks)
    (feathers/authentication #js{:storage (obj/get js/window "localStorage")}))

(def ^:dynamic *users* (feathers/service *app* "users"))

;; Helper Functions ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- verifyToken [res]
  (let [passport (obj/get *app* "passport")
        token (:accessToken (js->clj res :keywordize-keys true))]
        (.verifyJWT passport token)))

(defn- decodePayload [payload]
  (let [uid (:userId (js->clj payload :keywordize-keys true))
        user (svc/get *users* uid)]
    user))

(defn- setUser [user]
  (let [udat (js->clj user :keywordize-keys true)]
    (obj/set *app* "user" user)
    user))

;; Client Auth API ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn signup!
  ([email password]
    (signup! *users* email password))
  ([users email password]
    (svc/create users #js{:email email :password password})))

(defn login!
  ([email password]
    (login! *app* email password))
  ([app email password]
    (-> app
      (feathers/authenticate (clj->js {:strategy "local" :email email :password password}))
      (.then verifyToken)
      (.then decodePayload)
      (.then setUser))))

(defn logout!
  ([]
    (logout! *app*))
  ([app]
    (feathers/logout app)))

(defn auth!
  ([]
    (auth! *app*))
  ([app]
    (-> app
      (feathers/authenticate)
      (.then verifyToken)
      (.then decodePayload)
      (.then setUser)
      (.catch #(.error js/console (obj/get % "message"))))))

;; Client Service API ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn service
  ([svc] (service *app* svc))
  ([app svc] (feathers/service app svc)))

(def find svc/find)

(def get svc/get)

(def create svc/create)

(def update svc/update)

(def patch svc/patch)

(def remove svc/remove)

;; Client Event API ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def on svc/on)

(def created svc/created)

(def updated svc/updated)

(def patched svc/patched)

(def removed svc/removed)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
