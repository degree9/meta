(ns meta.ui.uikit
  (:require [hoplon.core :as hl]
            [hoplon.jquery]
            [javelin.core :as j]
            [meta.core :as core]
            [uikit-hl.button :as button]
            [uikit-hl.core :as uikit]
            [uikit-hl.card :as card]
            [uikit-hl.container :as container]
            [uikit-hl.dropdown :as dropdown]
            [uikit-hl.flex :as flex]
            [uikit-hl.form :as form]
            [uikit-hl.grid :as grid]
            [uikit-hl.icon :as icon]
            [uikit-hl.navbar :as navbar]
            [uikit-hl.offcanvas :as offcanvas]
            [uikit-hl.sticky :as sticky]
            [uikit-hl.tab :as tab]
            [uikit-hl.width :as width]
            [uikit-hl.visibility :as vis]
            [uikit-hl.utility :as util]))

(hl/defelem dashmenu [attr kids]
  (grid/grid :child-width-1-3-s true :child-width-1-4-m true
    (hl/for-tpl [x (:items attr)]
      (grid/cell :click #((:router attr) (key @x))
        (hl/div :class [:uk-text-center]
          (icon/icon :icon (j/cell= (:icon (val x))) :ratio 2)
          (hl/br)
          (hl/text "~{(:title (val x))}"))))))

(hl/defelem dashtoggle [attr kids]
  (navbar/toggle attr :uk-toggle "target: #offcanvas" kids))

(hl/defelem dashnav [attr kids]
  (navbar/container attr
    (container/container :expand true
      (navbar/navbar kids))))

(hl/defelem dashboard [attr kids]
  (offcanvas/content
    (offcanvas/offcanvas :overlay true :id "offcanvas"
      (offcanvas/bar (:offcanvas attr)))
    kids))
      ;(grid/grid :class [:uk-height-viewport :uk-margin-top] :match true :child-width-1-2-s true :child-width-1-3-m true
      ;  (hl/for-tpl [x (:items attr)]
      ;    (grid/cell
      ;      (card/card :default true))))
      ;        (card/body
      ;          (card/title
      ;            (icon/icon :icon (j/cell= (when x (:icon (val x)))) :ratio 2)
      ;            (hl/text "~{(when x (:title (val x)))}")))))

(hl/defelem login [attr kids]
  (let [submit!  (:submit attr)
        username (j/cell nil)
        password (j/cell nil)]
    (container/container :small true
      (hl/div :uk-height-viewport true :flex true :flex-middle true :flex-center true
        (card/card :default true :small true :css {:width "350px"}
          (card/header
            (card/title "Login"))
          (card/body
            (form/form
              (grid/grid :small true
                (grid/cell :width-1-1 true
                  (form/input :placeholder "Email" :blank true :change #(reset! username @%)))
                (grid/cell :width-1-1 true
                  (form/input :placeholder "Password" :type "password" :blank true :change #(reset! password @%))))))
          (card/footer
            (button/button :primary true :class [:uk-width-1-1] "Login" :click #(submit! @username @password))))))))

(hl/defelem app [attr kids]
  (let [attr (assoc attr :css {:background "#f9f9fb"})]
    (hl/html
      (hl/head
        (hl/link :rel "stylesheet" :href "https://cdnjs.cloudflare.com/ajax/libs/uikit/3.0.0-beta.35/css/uikit.min.css")
        (hl/link :rel "stylesheet" :href "/css/theme.css"))
      (hl/body attr kids))))
