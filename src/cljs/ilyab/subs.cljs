(ns ilyab.subs
  "The event subscriptions for the app."
  (:require [ilyab.db :as db]
            [re-frame.core :refer [reg-sub]]))

;; Subscribe to changes in the page (e.g. Home, About)
(reg-sub
  :page
  (fn [db _]
    (:page db)))

(reg-sub
  :docs
  (fn [db _]
    (:docs db)))

;; Subscribe to changes in the contact form
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
