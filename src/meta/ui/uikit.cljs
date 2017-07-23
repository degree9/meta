(ns meta.ui.uikit
  (:require [hoplon.core :as hl]
            [javelin.core :as j]
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
            ))

(hl/defelem dashmenu [attr kids]
  (grid/grid :child-1-2-s true :child-1-3-m true
    (hl/for-tpl [x (:items attr)]
      (grid/cell
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
            (dropdown/dropdown :uk-dropdown {:mode "click"} :navbar true :class [:uk-width-large]
              (dashmenu :items (:menu attr)))
            (:left attr)))
        (navbar/center
          (navbar/nav (:center attr)))
        (navbar/right
          (navbar/nav (:right attr)))))))

(hl/defelem dashboard [attr kids]
  (hl/div :class [:uk-height-viewport]
    (dashnav :menu (:menu attr) :left (:nav-left attr) :center (:nav-center attr) :right (:nav-right attr))
    (container/container
      (grid/grid :class [:uk-margin-top] :child-1-2-s true :child-1-3-m true
        (hl/for-tpl [x (:menu attr)]
          (grid/cell
            (card/card :default true
              (card/body
                (card/title
                  (icon/icon :icon (j/cell= (:icon x)) :ratio 2)
                  (hl/text "~{(:title x)}"))))))))))

(hl/defelem login [attr kids]
  (hl/div :class [:uk-height-viewport]
    (container/container :small true
      (card/card :default true :small true :css {:width "350px"}
        (card/header
          (hl/img :class [:uk-logo] :css {:height "85px" :width "225px"} :src "https://d9lounge.firebaseapp.com/images/logo.png"))
        (card/body
          (form/form
            (grid/grid :small true :width-expand true
              (hl/div :class [:uk-width-1-1]
                (form/input :placeholder "Email" :blank true))
              (hl/div :class [:uk-width-1-1]
                (form/input :placeholder "Password" :type "password" :blank true))
              (hl/div :class [:uk-width-1-2]
                (form/checkbox "Remember Me"))
              (hl/div :class [:uk-width-1-2]
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
