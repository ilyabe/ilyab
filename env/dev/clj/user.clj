(ns user
  (:require 
            [mount.core :as mount]
            [ilyab.figwheel :refer [start-fw stop-fw cljs]]
            ilyab.core))

(defn start []
  (mount/start-without #'ilyab.core/repl-server))

(defn stop []
  (mount/stop-except #'ilyab.core/repl-server))

(defn restart []
  (stop)
  (start))


