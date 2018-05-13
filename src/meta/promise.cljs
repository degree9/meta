(ns meta.promise
  (:refer-clojure :exclude [catch map resolve conj])
  (:require [goog.object :as obj])
  (:require-macros meta.promise))

;; Promise Protocol ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defprotocol IPromise
  "A simple protocol implementation for JavaScript Promises.

  If you need advanced promise features see funcool/promesa."
  (then    [_ callback] "Invokes callback with the result of the previous promise.")
  (catch   [_ callback] "Invokes callback with the result of the previous promise.")
  (log     [_]          "Output the result of the previous promise to the console.")
  (err     [_]          "Catch and output the error of a promise.")
  (map     [_ func]     "Map a function to the result of the previous promise.")
  (conj    [_ data]     "Conj data onto the result of the previous promise."))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Helpers ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn- promise-serial [current next]
  (then current
    (fn [data]
      (then (next)
        (fn [result] (cljs.core/conj data result))))))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Promise API ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn promise
  "Returns a new promise, optionally accepts a function as a promise constructor.

   Constructor should be a function of two arguments:
     * `resolve` - Used to resolve the promise.
     * `reject`  - Used to reject the promise.
  "
  ([] js/Promise.)
  ([callback] (js/Promise. callback)))

(defn resolve
  "Resolves the promise and returns value."
  ([value] (resolve (promise) value))
  ([promise value] (.resolve promise value)))

(defn reject
  "Rejects the promise with value as the reason."
  ([value] (reject (promise) (clj->js value)))
  ([promise value] (.reject promise (clj->js value))))

(defn all
  "Return a promise which resolves/rejects concurrent promises."
  ([promises]
   (all (promise) promises))
  ([promise promises]
   (.all promise (into-array promises))))

(defn serial
  "Return a promise which resolves/rejects consecutive promises."
  ([factories]
   (serial (promise) factories))
  ([promise factories]
   (then (reduce promise-serial (resolve promise []) factories) into-array)))

(extend-protocol IPromise
  default
  (then    [promise callback]   (then  (resolve promise) callback))
  (catch   [promise callback]   (catch (resolve promise) callback))
  (log     [promise]            (log   (resolve promise)))
  (err     [promise]            (err   (resolve promise)))
  (map     [promise func]       (map   (resolve promise) func))
  (conj    [promise data]       (conj  (resolve promise) data))

  js/Promise
  (then    [promise callback]   (.then  promise callback))
  (catch   [promise callback]   (.catch promise callback))
  (log     [promise]            (.then  promise #(.log js/console %)))
  (err     [promise]            (.catch promise #(.error js/console %)))
  (map     [promise func]       (.then  promise #(cljs.core/map func %)))
  (conj    [promise data]       (.then  promise #(cljs.core/conj % data))))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
