(ns app.services
  (:require [meta.server :as server]
            [meta.services :as services]
            [meta.services.hooks :as hooks]))

(def msg-pre-hooks {:all    []
                    :find   [(hooks/auth. #js["jwt" "local"])]
                    :get    [(hooks/auth. #js["jwt" "local"])]
                    :create []
                    :update []
                    :patch  []
                    :remove []})

(def msg-post-hooks {:all    []
                     :find   []
                     :get    []
                     :create []
                     :update []
                     :patch  []
                     :remove []})

(def msg-hooks {:before msg-pre-hooks :after msg-post-hooks})

(defn messages [app]
  (server/api app "messages" (:memory services/store) msg-hooks))
