(ns meta.promise
  (:refer-clojure :exclude [catch mapv resolve])
  (:require-macros meta.promise))

(defprotocol IPromise
  "A CLJS wrapper around JavaScript promises."
  (all     [_ promises] "Return a promise which resolves/rejects concurrent promises.")
  (then    [_ callback] "Invokes callback with the result of the previous promise.")
  (catch   [_ callback] "Invokes callback with the result of the previous promise.")
  (resolve [_ value]    "Resolves the promise and returns value.")
  (reject  [_ value]    "Rejects the promise with value as the reason.")
  (log     [_]          "Output the result of the previous promise to the console.")
  (err     [_]          "Catch and output the error of a promise."))

(extend-protocol IPromise
  default
  (all     [promise promises] (.all     promise (clj->js promises)))
  (then    [promise callback] (.then    promise callback))
  (catch   [promise callback] (.catch   promise callback))
  (resolve [promise value]    (.resolve promise (clj->js value)))
  (reject  [promise value]    (.reject  promise (clj->js value)))
  (log     [promise]          (.then    promise #(.log js/console %)))
  (err     [promise]          (.catch   promise #(.error js/console %))))

(defn promise
  "Returns a new promise where callback is a constructor."
  ([] js/Promise.)
  ([callback] (js/Promise. callback)))

(defn mapv [f l]
  (cljs.core/mapv
    (fn [& args]
      (promise
        (fn [res rej]
          (res (apply f args))))) l))

(defn map-all [func ls]
  (all (promise) (mapv func ls)))
