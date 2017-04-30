(ns meta.services.hooks
  (:require [feathers.authentication :as auth]))

(def auth (:authenticate auth/hooks))

(def hashpass (:hashPassword auth/hooks))
