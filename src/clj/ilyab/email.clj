(ns ilyab.email
  "The functions for this namespace."
  (:require [postal.core :refer [send-message]]))

(defn send-msg
  "Function to take a subject and a message body `msg` and send them off to my
  email address. Returns `nil` if there's an error and a map on success:
    {:message \"message sent\" :status \"OK\"}
  "
  [s msg]
  (let [res (send-message {:from "ilya@ilyab.com"
                           :to "ilyabe@gmail.com"
                           :subject s
                           :body msg})]
    (if (= :SUCCESS (:error res))
      (-> res
          (select-keys [:message])
          (assoc :status "OK")))))
