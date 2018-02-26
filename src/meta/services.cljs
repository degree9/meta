(ns meta.services
  (:refer-clojure :exclude [find get update remove])
  (:require [feathers.services :as svc]))

(def service svc/service)

(def find svc/find)

(def get svc/get)

(def create svc/create)

(def update svc/update)

(def patch svc/patch)

(def remove svc/remove)
