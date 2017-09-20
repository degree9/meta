(ns meta.services.hooks
  (:require [feathers.authentication :as auth]))

(prn auth/hooks)
(def authenticate (:authenticate auth/hooks))
(def hash-password (:hashPassword auth/hooks))
