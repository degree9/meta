(ns meta.promise)

(defmacro defpromise [name args & body]
  `(def ~name (meta.promise/promise (fn ~args ~@body))))
