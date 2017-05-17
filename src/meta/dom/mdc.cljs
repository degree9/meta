(ns meta.dom.mdc
  (:require [hoplon.core :as h]
            [javelin.core :as j]
            [material-hl.core :as mwc]
            [material-hl.drawer :as drawer]
            [material-hl.toolbar :as toolbar]))

(h/defelem temporary-drawer [attr kids]
  (drawer/temporary
    (drawer/temporary-drawer
      (drawer/temporary-header
        (drawer/temporary-header-content "Drawer Header"))
      (drawer/temporary-content "Drawer Content"))))

(h/defelem persistent-drawer [attr kids]
  (drawer/persistent
    (drawer/persistent-drawer
      (drawer/persistent-header
        (drawer/persistent-header-content "Drawer Header"))
      (drawer/persistent-content "Drawer Content"))))

(h/defelem permanent-drawer [attr kids]
  (drawer/permanent
    (drawer/permanent-toolbar-spacer)
    (drawer/permanent-content "Drawer Content")))

(h/defelem toolbar [attr kids]
  (toolbar/toolbar :fixed true
    (toolbar/row
      (toolbar/section kids))))

(h/defelem content [attr kids]
  (toolbar/content :fixed true))

(h/defelem head [attr kids]
  (let [title (:title attr)]
    (h/head attr
      (h/html-meta :http-equiv "content-type" :content "text/html; charset=utf-8")
      (h/html-meta :name "viewport" :content "width=device-width, initial-scale=1.0, maximum-scale=1, user-scalable=0")
      (h/title title)
      (h/link :rel "shortcut icon" :href "favicon.ico")
      (h/link :rel "stylesheet" :href "https://unpkg.com/material-components-web@0.10.0/dist/material-components-web.css")
      kids)))

(h/defelem body [attr kids]
  (let [drawer (:drawer attr :temporary)
        tb-items (:toolbar attr)]
    (h/body
      (assoc attr :css {:min-height "100%" :padding "0" :margin "0" :box-sizing "border-box"})
      (h/case-tpl drawer
        :temporary  [(toolbar) (temporary-drawer) (content kids)]
        :persistent [(persistent-drawer) (content (toolbar) kids)]
        :permanent-above  [(permanent-drawer)]
        :permanent-below  [(permanent-drawer)]))))

(h/defelem app [attr kids]
  (let [drawer (:drawer attr :temporary)]
    (h/html
      (head)
      (body attr kids))))


;(route-tpl routes
;  :chat    (chat/chat)
;  :landing (view/landing)
;  :login   (view/login)
;  :signup  (view/signup)
;  :missing "404!")
