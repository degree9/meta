(ns meta.nobackend.firebase
  (:require [meta.core :as m]
            [javelin.core :as j]
            [firebase-cljs.core :as fb]
            [firebase-cljs.database :as fbdb]
            [firebase-cljs.database.query :as fbdq]
            [firebase-cljs.database.datasnapshot :as fbsnap]
            [firebase-cljs.user :as fbuser]
            [hoplon.firebase :as hfb]
            [hoplon.firebase.auth :as hfba]))

;; Firebase Auth State ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def ^:dynamic *user-auth*    hfba/*user-auth*)

(def ^:dynamic *user-cred*    hfba/*user-cred*)

(def ^:dynamic *user-fb*      hfba/*user-fb*)

(def ^:dynamic *pending-link* hfba/*pending-link*)

;; Firebase Root ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def ^:dynamic *firebase* (hfb/fb-ref [:_meta]))

;; Firebase Public References ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn settings
  ([] (hfb/fb-ref *firebase* [:settings]))
  ([korks] (fbdb/get-in (settings) korks)))

(defn settings=
  [] (hfb/fb-cell (settings)))

(defn navigation
  ([] (hfb/fb-ref *firebase* [:navigation]))
  ([korks] (fbdb/get-in (navigation) korks)))

(defn navigation=
  [] (hfb/fb-cell (navigation)))

(defn user=
  [] (j/cell= (merge *user-auth* *user-fb*)))

(defn users
  ([] (hfb/fb-ref *firebase* [:users]))
  ([korks] (fbdb/get-in (users) korks)))

(defn users=
  [] (hfb/fb-cell (users)))

(defn dashboard
  ([] (hfb/fb-ref *firebase* [:dashboard]))
  ([korks] (fbdb/get-in (dashboard) korks)))

(defn dashboard= []
  (let [fbc (j/cell nil)]
    (j/cell=
      (fbdb/listen-once
        (if (empty? m/*route*) (dashboard)
          (fbdb/get-in *firebase* m/*route*))
        "value"
        ~#(reset! fbc (hfb/fb->clj %))))
    (j/cell= fbc)))

;; Firebase Authentication ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def auth! #(hfba/fb-auth! (users) {:enabled true}))
