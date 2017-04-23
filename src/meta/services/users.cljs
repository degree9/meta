(ns meta.services.users
  (:require [meta.services :as services]
            [meta.services.hooks :as hooks]))

(def pre-hooks {:all    []
                :find   [(hooks/auth. #js["jwt" "local"])]
                :get    [(hooks/auth. #js["jwt" "local"])]
                :create [(hooks/hashpass.)]
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
  (let [storef (get services/store store (:memory services/store))]
    (feathers/api app "users" (storef) user-hooks)))
