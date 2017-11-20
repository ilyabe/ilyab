(ns ilyab.events
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

;;;;; subscriptions ;;;;;

(reg-sub
  :page
  (fn [db _]
    (:page db)))

(reg-sub
  :docs
  (fn [db _]
    (:docs db)))

(reg-sub
  :contact
  (fn [db _]
    (:contact db)))

;; Subscribe to changes in the subject line of the contact form
(reg-sub
  :subject
  (fn [db]
    (:subject db)))

;; Subscribe to changes in the message line of the contact form
(reg-sub
  :message
  (fn [db]
    (:message db)))

;; Subscribe to changes in my name
(reg-sub
 :name
 (fn [db]
   (:name db)))

;;;;; event handlers ;;;;;

;; Handles the event fired when the contact form is submitted
(reg-event-db
  :contact-submit
  (fn [db [_ _]] ;; TODO what are these two args _ _?
    (.log js/console (:subject db) (:message db))
    (POST "/v1/contact"
      {:params (select-keys db [:subject :message])
       :handler #(dispatch [:contact-success %1])
       :error-handler #(dispatch [:contact-error %1])})
    db))

;; Handles successful responses from submitting the contact form
(reg-event-db
  :contact-success
  (fn [db [_ resp]]
    ;; TODO handle success
    (.log js/console (str "contact-success: " resp))
    (update db :contact #(assoc % :status :sent :msg "Message sent. Thanks! :-)"))))

;; Handles successful error responses from submitting the contact form
(reg-event-db
  :contact-error
  (fn [db [_ resp]]
    ;; TODO handle error
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
  (fn [db [_ _]]
    (.log js/console "Trying again")
    (update db :contact #(assoc % :status :open))))
