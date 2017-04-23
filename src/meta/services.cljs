(ns meta.services
  (:require [feathers.app :as feathers]
            [feathers.memory :as memory]
            [feathers.hooks :as hooks]))

(def store {:memory memory/memory})
