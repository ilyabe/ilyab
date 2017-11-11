(ns ilyab.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [secretary.core :as secretary]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [ajax.core :refer [GET POST]]
            [ilyab.ajax :refer [load-interceptors!]]
            [ilyab.events])
  (:import goog.History))

(defn nav-link [uri title page collapsed?]
  (let [selected-page (rf/subscribe [:page])]
    [:li.nav-item
     {:class (when (= page @selected-page) "active")}
     [:a.nav-link
      {:href uri
       :on-click #(reset! collapsed? true)} title]]))

(defn navbar []
  (r/with-let [collapsed? (r/atom true)]
    [:nav.navbar.navbar-dark.bg-primary
     [:button.navbar-toggler.hidden-sm-up
      {:on-click #(swap! collapsed? not)} "☰"]
     [:div.collapse.navbar-toggleable-xs
      (when-not @collapsed? {:class "in"})
      [:a.navbar-brand {:href "#/"} "ilyab"]
      [:ul.nav.navbar-nav
       [nav-link "#/" "Home" :home collapsed?]
       [nav-link "#/about" "About" :about collapsed?]]]]))

(defn about-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     [:img {:src (str js/context "/img/warning_clojure.png")}]]]])

;;(defn home-page []
;;  [:div.container
;;   (when-let [docs @(rf/subscribe [:docs])]
;;     [:div.row>div.col-sm-12
;;      [:div {:dangerouslySetInnerHTML
;;             {:__html (md->html docs)}}]])])

(defn subtitle
  "The blurb below my name."
  []
  [:p "Website created with "
    [:a {:href "https://clojure.org"} "Clojure"]
    ", "
    [:a {:href "https://clojurescript.org"} "ClojureScript"]
    ", and "
    [:a {:href "https://github.com/Day8/re-frame"} "re-frame"]
    "."])

(defn headshot
  "My main image."
  []
  [:img.headshot {:src "img/sargon.jpg" :width 100 :height 100}])

(defn my-name
  "My name, of course."
  []
  (let [nm (rf/subscribe [:name])]
    [:h1 @nm]))

(defn contact-form
  "The form for submitting a comment."
  []
  [:form.contact-form {:action "/v1/contact", :method "post"}
    [:label "Subject: "] [:input {:type "text"
                                  :name "subject"
                                  :on-change #(rf/dispatch [:subj-change (-> % .-target .-value)])}]
    [:label "Message: "] [:textarea {:rows "4"
                                     :cols "50"
                                     :name "message"
                                     :on-change #(rf/dispatch [:msg-change (-> % .-target .-value)])}]
    [:input {:type "button",
             :value "Send"
             :on-click #(rf/dispatch [:contact-submit])
            }]])

(defn home-page
  []
  [:div.container
    [my-name]
    [headshot]
    [subtitle]
    [contact-form]])

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
