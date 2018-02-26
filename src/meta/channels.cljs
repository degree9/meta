(ns meta.channels
  (:require [feathers.channels :as chan]))

(defn join-anonymous [app]
  (chan/connection app #(chan/join (chan/channel app "anonymous") %)))
