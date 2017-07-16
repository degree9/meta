(ns meta.ui
  (:require [meta.ui.mdc :as mdc]
            [meta.ui.uikit :as uikit]))

(defn app [& opts]
  (let [style (:style opts :uikit)]
    (case style
      :mdc (apply mdc/app opts)
      :uikit (apply uikit/app opts))))
