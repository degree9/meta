(ns meta.promise)

(defmacro defpromise [args & body]
  `(meta.promise/promise (fn ~args ~@body)))

(defmacro prom-> [& body]
  `(let [promise# js/Promise.]
    (-> promise# ~@body)))
