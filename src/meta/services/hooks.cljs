(ns meta.services.hooks
  (:require [feathers.authentication :as auth]))

(def authenticate (:authenticate auth/hooks))
