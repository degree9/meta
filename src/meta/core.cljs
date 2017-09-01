(ns meta.core)

(def ^:dynamic *route*
  "A thread bound formula cell to the current route handler."
  nil)

(def ^:dynamic *route-params*
  "A thread bound formula cell to the current route params."
  nil)

(def ^:dynamic *router*
  "A thread bound router function, which should change the current route."
  nil)

(def ^:dynamic *navigation*
  "A thread bound formula cell to a list of navigation links."
  nil)

(def ^:dynamic *permissions*
  "A thread bound formula cell to a list of permissions."
  nil)

(def ^:dynamic *user*
  "A thread bound formula cell containing a user map."
  nil)

(def ^:dynamic *users*
  "A thread bound formula cell containing a list of users."
  nil)

(def ^:dynamic *data*
  "A thread bound formula cell containing data to be displayed."
  nil)
