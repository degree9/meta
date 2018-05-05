(ns meta.routing
  (:require [meta.routing.history :as history]))


(def ^:dynamic *router* :history)

(defn route= [& [router]]
  (case (or router *router*)
    :history (history/route=)))

(defn router [& [router]]
  (case (or router *router*)
    :history history/router))
