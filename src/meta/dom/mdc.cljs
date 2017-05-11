(ns meta.dom.mdc
  (:require [hoplon.core :as h]
            [javelin.core :as j]
            [hoplon.bidi]
            [material-hl.core :as mwc])
  (:require-macros [hoplon.bidi :refer [route-tpl]]))

(h/defelem material-head [attr kids]
  (let [title (:title attr)]
    (h/head attr
      (html-meta :http-equiv "content-type" :content "text/html; charset=utf-8")
      (html-meta :name "viewport" :content "width=device-width, initial-scale=1.0, maximum-scale=1, user-scalable=0")
      (title title)
      ;(link :rel "shortcut icon" :href "favicon.png")
      (link :rel "stylesheet" :href "css/material-components-web.css"))
      kids)))

(h/defelem material-body [attr kids]
  (let []
    (body
      (assoc attr :style {:min-height "100%"})
      (route-tpl routes
        :chat    (chat/chat)
        :landing (view/landing)
        :login   (view/login)
        :signup  (view/signup)
        :missing "404!"))))

(h/defelem material-app [attr kids]
  (let [style (:style attr)]
    (h/html
      (material-head)
      (window-body kids))))
