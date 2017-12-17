(ns meta.ui
  (:require [hoplon.core :as h :include-macros true]))

;;; DOM Macros ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defmacro defelem [& args]
  `(h/defelem ~@args))

(defmacro case-tpl [& args]
  `(h/case-tpl ~@args))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
