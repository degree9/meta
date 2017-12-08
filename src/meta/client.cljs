(ns meta.client
  (:refer-clojure :exclude [find get update remove])
  (:require [cljsjs.socket-io]
            [goog.object :as obj]
            [javelin.core :as j]
            [feathers.client :as feathers]
            [feathers.client.services :as svc]))

;; Feathers Client ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def ^:dynamic *app* (feathers/feathers))

(-> *app*
    (feathers/socketio (js/io))
    (feathers/hooks)
    (feathers/authentication #js{:storage (obj/get js/window "localStorage")}))

(def ^:dynamic *users* (feathers/service *app* "/users"))

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
  (let [udat (if (array? user) (first user) user)]
    (obj/set *app* "user" udat)
    udat))

(defn- handle-auth [auth]
  (-> auth
    (.then verifyToken)
    (.then decodePayload)
    (.then setUser)))

;; Client Auth API ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn signup!
  ([email password]
   (signup! *users* email password))
  ([users email password]
   (svc/create users #js{:email email :password password})))

(defn login!
  ([email password]
   (login! "local" email password))
  ([strategy email password]
   (login! *app* strategy email password))
  ([app strategy email password]
   (-> app
     (feathers/authenticate (clj->js {:strategy strategy :email email :password password}))
     (handle-auth))))

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
     (handle-auth)
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
