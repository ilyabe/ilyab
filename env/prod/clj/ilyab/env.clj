(ns ilyab.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[ilyab started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[ilyab has shut down successfully]=-"))
   :middleware identity})
