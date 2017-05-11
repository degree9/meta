(ns meta.dom
  (:require [meta.dom.mdc :as mdc]))

(defn app [& opts]
  (let [type (:type opts :mdc)]
    (case type
      :mdc (apply mdc/app opts))))
