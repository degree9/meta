(ns app.server
  (:require [meta.server :as server]
            [app.services :as services]))

(-> server/app
    {{#services}}
    {{.}}
    {{/services}})

(defn- main []
  (server/listen server/app "8080"))

(defn init []
  (server/init! main))
