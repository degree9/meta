(ns meta.ui.uikit
  (:require [hoplon.core :as hl]
            [javelin.core :as j]
            [meta.core :as core]
            [uikit-hl.button :as button]
            [uikit-hl.core :as uikit]
            [uikit-hl.card :as card]
            [uikit-hl.container :as container]
            [uikit-hl.dropdown :as dropdown]
            [uikit-hl.form :as form]
            [uikit-hl.grid :as grid]
            [uikit-hl.icon :as icon]
            [uikit-hl.navbar :as navbar]
            [uikit-hl.tab :as tab]
            [uikit-hl.width :as width]
            ))

(hl/defelem dashmenu [attr kids]
  (grid/grid :child-width-1-3-s true :child-width-1-4-m true
    (hl/for-tpl [x (:items attr)]
      (grid/cell :click #((:router attr) (key @x))
        (hl/div :class [:uk-text-center]
          (icon/icon :icon (j/cell= (:icon (val x))) :ratio 2)
          (hl/br)
          (hl/text "~{(:title (val x))}"))))))

(hl/defelem dashnav [attr kids]
  (navbar/container
    (container/container :expand true
      (navbar/navbar
        (navbar/left
          (navbar/nav
            (navbar/toggle)
            (dropdown/dropdown :uk-dropdown {:mode "click"} :navbar true :width-xlarge true
              (dashmenu :items (:menu attr) :router (:router attr)))
            (:left attr)))
        (navbar/center
          (navbar/nav (:center attr)))
        (navbar/right
          (navbar/nav (:right attr)))))))

(hl/defelem dashboard [attr kids]
  (hl/div :class [:uk-height-viewport]
    (dashnav :menu (:menu attr) :left (:nav-left attr) :center (:nav-center attr) :right (:nav-right attr) :router (:router attr))
    (container/container
      (grid/grid :class [:uk-margin-top] :match true :child-width-1-2-s true :child-width-1-3-m true
        (hl/for-tpl [x (:items attr)]
          (grid/cell
            (card/card :default true
              (card/body
                (card/title
                  (icon/icon :icon (j/cell= (when x (:icon (val x)))) :ratio 2)
                  (hl/text "~{(when x (:title (val x)))}"))))))))))

(hl/defelem login [attr kids]
  (hl/div :class [:uk-height-viewport]
    (container/container :small true
      (card/card :default true :small true :css {:width "350px"}
        (card/header
          (hl/img :class [:uk-logo] :css {:height "85px" :width "225px"} :src "https://d9lounge.firebaseapp.com/images/logo.png"))
        (card/body
          (form/form
            (grid/grid :small true :width-expand true
              (hl/div :width-1-1
                (form/input :placeholder "Email" :blank true))
              (hl/div :width-1-1
                (form/input :placeholder "Password" :type "password" :blank true))
              (hl/div :width-1-1
                (form/checkbox "Remember Me"))
              (hl/div :width-1-2
                (button/button :text true "Reset Password?")))))
        (card/footer
          (button/button :primary true :class [:uk-width-1-1] "Login")))
      (hl/div))))

(hl/defelem app [attr kids]
  (let [attr (assoc attr :css {:background "#f9f9fb"})]
    (hl/html
      (hl/head
        (hl/link :rel "stylesheet" :href "https://cdnjs.cloudflare.com/ajax/libs/uikit/3.0.0-beta.25/css/uikit.min.css"))
      (hl/body attr kids))))
