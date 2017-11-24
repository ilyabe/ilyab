(ns ilyab.routes.home
  (:require [clojure.tools.logging :refer [info infof warn warnf error errorf]]
            [clojure.java.io :as io]
            [compojure.core :refer [defroutes GET POST]]
            [ilyab.email :as email]
            [ilyab.layout :as layout]
            [ring.util.http-response :as response]))

(defn home-page []
  (layout/render "home.html"))

(defroutes home-routes
  (GET "/" []
       (home-page))
  (GET "/docs" []
       (-> (response/ok (-> "docs/docs.md" io/resource slurp))
           (response/header "Content-Type" "text/plain; charset=utf-8")))
  ;; TODO spec
  (POST "/v1/contact" {{:keys [subject message]} :params}
    (infof "Got params: %s %s" subject message)
    (if (every? empty? [subject message])
      (response/bad-request "Missing subject or body")
      (let [res (email/send-msg subject message)]
        (if (= "OK" (:status res))
          (response/ok (merge res {:subject subject, :message message}))
          (response/internal-server-error res))))))
