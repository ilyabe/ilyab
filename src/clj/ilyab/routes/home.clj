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
  (POST "/v1/contact" {{:keys [subject message]} :params}
    (if (and subject message)
      (let [res (email/send! subject message)]
        (if (map? res)
          (response/ok res)
          (response/internal-server-error res)))
      (response/bad-request "Missing subject or body"))))
