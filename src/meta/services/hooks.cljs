(ns meta.services.hooks
  (:require [feathers.authentication :as auth]))

(defn auth (:authenticate auth/hooks)

(defn hashpass (:hashPassword auth/hooks))
