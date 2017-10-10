(ns meta.services
  (:require [feathers.memory :as memory]
            [feathers.mailgun :as mailgun]
            [feathers.hooks :as hooks]))

(def memory memory/memory)

(def mailgun mailgun/mailgun)
