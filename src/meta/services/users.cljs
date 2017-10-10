(ns meta.services.users
  (:require [feathers.app :as feathers]
            [meta.services :as services]
            [meta.services.hooks :as hooks]))

(def pre-hooks {:all    []
                :find   [(hooks/authenticate. #js["jwt" "local"])]
                :get    [(hooks/authenticate. #js["jwt" "local"])]
                :create [(hooks/hash-password.)]
                :update []
                :patch  []
                :remove []})

(def post-hooks {:all    []
                 :find   []
                 :get    []
                 :create []
                 :update []
                 :patch  []
                 :remove []})

(def user-hooks {:before pre-hooks :after post-hooks})

(defn users [app & [store]]
  (let [storef (or store services/memory)]
    (feathers/api app "users" (storef) user-hooks)))
