(ns meta.routing.history
  (:require [hoplon.history :as history]
            [javelin.core :as j]))

(def ^:dynamic *history* (history/history-cell))

(defn route= [] (j/cell= *history*))

(defn router [route] (reset! *history* (str (name route))))
