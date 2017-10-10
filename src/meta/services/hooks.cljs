(ns meta.services.hooks
  (:require [feathers.authentication :as auth]
            [feathers.hooks :as hooks]))

;; Feathers Authentication Hooks ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def authenticate (:authenticate auth/hooks))
(def hash-password (:hashPassword auth/hooks))
(def addVerification (:addVerification auth/hooks))
(def removeVerification (:removeVerification auth/hooks))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Feathers Common Hooks ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def iff            hooks/iff)
(def isProvider     hooks/isProvider)
(def preventChanges hooks/preventChanges)
(def disallow       hooks/disallow)
