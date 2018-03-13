(ns meta.nobackend
  (:require [meta.nobackend.firebase :as firebase]
            [meta.nobackend.hoodie :as hoodie]
            [meta.nobackend.parse :as parse]))


(def ^:dynamic *provider* :firebase)

(defn navigation= [& [provider]]
  (case (or provider *provider*)
    :firebase (firebase/navigation=)))

(defn user= [& [provider]]
  (case (or provider *provider*)
    :firebase (firebase/user=)))

(defn users= [& [provider]]
  (case (or provider *provider*)
    :firebase (firebase/users=)))

(defn dashboard= [& [provider]]
  (case (or provider *provider*)
    :firebase (firebase/dashboard=)))

(defn auth! [& [provider]]
  (case (or provider *provider*)
    :firebase (firebase/auth!)))
