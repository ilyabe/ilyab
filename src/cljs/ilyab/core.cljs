(ns ilyab.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [secretary.core :as secretary]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [ajax.core :refer [GET POST]]
            [ilyab.ajax :refer [load-interceptors!]]
            [ilyab.events]
            [ilyab.subs])
  (:import goog.History))

(defn nav-link
  [uri title page]
  (let [selected-page (rf/subscribe [:page])]
    [:li.nav-item
     {:class (when (= page @selected-page) "active")}
     [:a.nav-link {:href uri} title]]))

(defn navbar
  []
  [:nav.navbar.navbar-expand-lg.navbar-dark.bg-primary
   [:a.navbar-brand {:href "#/"} "~/ilya"]
    [:button.navbar-toggler
    {:type "button"
     :data-toggle "collapse"
     :data-target "#navbarSupportedContent"
     :aria-controls "navbarSupportedContent"
     :aria-expanded "false"
     :aria-label "Toggle navigation"}
    [:span.navbar-toggler-icon]]
   [:div#navbarSupportedContent.collapse.navbar-collapse
    [:ul.navbar-nav.mr-auto
     [nav-link "#/" "Home" :home]
     [nav-link "#/about" "About" :about]]]])

(defn about-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     [:img {:src (str js/context "/img/warning_clojure.png")}]]]])

(defn subtitle
  "The blurb below my name."
  []
  [:div.row
   [:div.col.text-center
    [:h6.card-subtitle.mb-2.text-muted
     "Website created with "
     [:a {:href "https://clojure.org", :target "_blank"} "Clojure"]
     ", "
     [:a {:href "https://clojurescript.org", :target "_blank"} "ClojureScript"]
     ", "
     [:a {:href "https://github.com/Day8/re-frame", :target "_blank"} "re-frame"]
     ", and "
     [:a {:href "http://getbootstrap.com/", :target "_blank"} "Bootstrap"]
     "."]]])

(defn icons
  "The social icons."
  []
  [:div.row
   [:div.col.text-center
    [:a {:href "https://github.com/ilyabe", :target "_blank"}
     [:i.fa.fa-github.social-icons {:aria-hidden "true"}]]]
    [:div.col.text-center
     [:a {:href "https://www.linkedin.com/in/ilya-bernshteyn-01b2317", :target "_blank"}
      [:i.fa.fa-linkedin-square.social-icons {:aria-hidden "true"}]]
      ]])

(defn headshot
  "My main image."
  []
  [:div.row
   [:div.col.text-center
    [:img.rounded.headshot
     {:src "img/sargon.jpg"
      :alt "Ilya Bernshteyn"}]]])

(defn my-name
  "My name, of course."
  []
  (let [nm (rf/subscribe [:name])]
    [:div.row.justify-content-center
     [:div.col.text-center
      [:h1.name-heading @nm]]]))

(defn contact-form
  "The form for submitting a message."
  []
  (let [c (rf/subscribe [:contact])
        subj (rf/subscribe [:subject])
        msg (rf/subscribe [:message])]
    (if (= :open (:status @c))
      [:form.contact-form {:action "/v1/contact", :method "post"}
       [:div.form-group
        [:label {:for "subject"} "Subject"]
        [:input#subject.form-control
         {:type "text"
          :name "subject"
          :placeholder "Subject"
          :value @subj
          :on-change #(rf/dispatch [:subj-change (-> % .-target .-value)])}]]
       [:div.form-group
        [:label {:for "message"} "Message"]
        [:textarea#message.form-control
         {:rows "4"
          :name "message"
          :placeholder "Message"
          :value @msg
          :on-change #(rf/dispatch [:msg-change (-> % .-target .-value)])}]]
       [:button.btn.btn-primary
        {:type "button"
         :on-click #(rf/dispatch [:contact-submit])}
        "Send"]])))

(defn contact-result
  "The results of submitting the contact form."
  []
  (let [c (rf/subscribe [:contact])]
    (case (:status @c)
      :sent [:div.alert.alert-success {:role "alert"} (:msg @c)
             [:a.badge.badge-primary.try-btn {:href "#", :on-click #(rf/dispatch [:contact-again :clear])} "Send another?"]]
      :error [:div.alert.alert-danger {:role "alert"} (:msg @c)
              " "
              [:a.badge.badge-primary.try-btn {:href "#", :on-click #(rf/dispatch [:contact-again])} "Try again?"]]
      nil)))

(defn home-page
  []
  [:div.container
    [headshot]
    [my-name]
    [subtitle]
    [icons]
    [contact-form]
    [contact-result]])

(def pages
  {:home #'home-page
   :about #'about-page})

(defn page []
  [:div
   [navbar]
   [(pages @(rf/subscribe [:page]))]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (rf/dispatch [:set-active-page :home]))

(secretary/defroute "/about" []
  (rf/dispatch [:set-active-page :about]))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
      HistoryEventType/NAVIGATE
      (fn [event]
        (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn fetch-docs! []
  (GET "/docs" {:handler #(rf/dispatch [:set-docs %])}))

(defn mount-components []
  (rf/clear-subscription-cache!)
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch-sync [:initialize-db])
  (load-interceptors!)
  (fetch-docs!)
  (hook-browser-navigation!)
  (mount-components))
