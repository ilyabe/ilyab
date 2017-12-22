(ns ilyab.events
  "The event dispatchers and handlers for the app."
  (:require [ajax.core :refer [POST]]
            [ilyab.db :as db]
            [re-frame.core :refer [dispatch reg-event-db reg-sub]]))

;;;;; dispatchers ;;;;;

(reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))

(reg-event-db
  :set-active-page
  (fn [db [_ page]]
    (assoc db :page page)))

(reg-event-db
  :set-docs
  (fn [db [_ docs]]
    (assoc db :docs docs)))

;;;;; event handlers ;;;;;

;; Handles the event fired when the contact form is submitted
(reg-event-db
  :contact-submit
  (fn [db [_ _]] ;; TODO what are these two args _ _?
    (.log js/console (:subject db) (:message db))
    (if (and (:subject db) (:message db))
      (do (POST "/v1/contact"
            {:params (select-keys db [:subject :message])
             :handler #(dispatch [:contact-success %1])
             :error-handler #(dispatch [:contact-error %1])})
          (assoc-in db [:contact :status] :sending))
      (assoc-in db [:contact :validated] true))))

;; Handles successful responses from submitting the contact form
(reg-event-db
  :contact-success
  (fn [db [_ resp]]
    (.log js/console (str "contact-success: " resp))
    (update db :contact #(assoc % :status :sent
                                  :msg "Message sent. Thanks! :-)"
                                  :validated false))))

;; Handles successful error responses from submitting the contact form
(reg-event-db
  :contact-error
  (fn [db [_ resp]]
    (.log js/console (str "Got error: " resp))
    (update db :contact #(assoc % :status :error :msg "Oops! Something went wrong :-("))))

;; Handles the event fired when the subject on the contact form changes
(reg-event-db
  :subj-change
  (fn [db [_ s]]
    (assoc db :subject s)))

;; Handles the event fired when the message on the contact form changes
(reg-event-db
  :msg-change
  (fn [db [_ s]]
    (.log js/console s)
    (assoc db :message s)))

;; Handles the event fired when the user clicks to retry the contact form
(reg-event-db
  :contact-again
  (fn [db [_ clr]]
    (let [clear? (= :clear clr)]
      (-> db
          (update :contact #(assoc % :status :open))
          (update :subject #(if clear? nil (:subject db)))
          (update :message #(if clear? nil (:message db)))))))
