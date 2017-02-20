(ns meta.boot.util
  (:require [boot.util :as util]
            [clojure.string :as s]))

(defn info-item
  ([msg]
    (info-item "•" msg))
  ([pre msg]
    (util/info "%s %s\n" pre msg))
  ([step total msg]
    (util/info "[%s/%s] %s\n" step total msg)))

(defn info-list [msgs]
  (doseq [msg msgs]
    (info-item msg)))

(defn info-progress [msgs]
  (doseq [[index msg] msgs]
    (info-item (-> index name str) (count msgs) msg)))

(defn info
  ([msg]
    (util/info "%s\n" msg))
  ([task msg]
    (info-item task msg))
  ([task msg list]
    (info task msg)
    (if (sorted? list)
      (info-progress list)
      (info-list list))))
