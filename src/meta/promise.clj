(ns meta.promise)

(defmacro defpromise [name args & body]
  `(def ~name (meta.promise/promise (fn ~args ~@body))))

(defmacro with-callback [cb & body]
  `(meta.promise/promise
    (fn [resolve# reject#]
      (let [~cb (fn [err# result#] (if err# (reject# err#) (resolve# result#)))]
        ~@body))))
