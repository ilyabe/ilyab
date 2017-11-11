(ns ilyab.routes.home
  (:require [ilyab.layout :as layout]
            [clojure.tools.logging :refer [info infof warn warnf error errorf]]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]))

(defn home-page []
  (layout/render "home.html"))

(defroutes home-routes
  (GET "/" []
       (home-page))
  (GET "/docs" []
       (-> (response/ok (-> "docs/docs.md" io/resource slurp))
           (response/header "Content-Type" "text/plain; charset=utf-8")))
  (POST "/v1/contact" [:as {params :params}]
    (infof "Got params: %s" params)
    (response/ok params)))
