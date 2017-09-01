(ns meta.routing.history
  (:require [hoplon.history :as history]
            [javelin.core :as javelin]))

(def ^:dynamic *history* (history/history-cell))

(defn route= [] (javelin/cell= *history*))

(defn router [route] (reset! *history* (str (name route))))
