(ns ilyab.email
  "Functions for sending emails."
  (:require [amazonica.aws.simpleemail :as ses]
            [amazonica.core :refer [defcredential]]
            [clojure.tools.logging :refer [error]]
            [environ.core :refer [env]]
            [postal.core :refer [send-message]]))

(def email-key
  "The AWS key for the SES user. Read from the EMAIL_KEY environment variable
  in production or the `:email-key` in the `profiles.clj` locally."
  (env :email-key))

(def email-secret
  "The AWS secret for the SES user. Read from the EMAIL_SECRET environment variable
  in production or the `:email-key` in the `profiles.clj` locally."
  (env :email-secret))

;; Convenience macro from amazonica to use the same credentials in all AWS calls
;; in this namespace
(defcredential email-key email-secret "us-east-1")

(defn send!
  "Function to take two strings -- a subject `s` and a `body` and send an email
  off to my email address. Returns `nil` if there's an error sending the email
  via SES and likewise if either the subject or body are `nil`. If successful,
  returns a map like this:

    {:body \"hey\"
     :message-id \"010001605a991020-bb67a6fe-5295-4a05-98a0-744a970b5dfc-000000\"
     :status \"OK\"
     :subject \"hey\"}

  The from and to are hard-coded and there are no retries because I don't need
  much more here right now."
  [s body]
  (if (and s body)
    (let [resp (try (ses/send-email :destination {:to-addresses ["ilyabe@gmail.com"]}
                                                  :source "ilya@ilyab.com"
                                                  :message {:subject s, :body {:text body}})
                 (catch Throwable t (error t)))]
      (when resp (merge resp {:subject s, :body body, :status "OK"})))))
