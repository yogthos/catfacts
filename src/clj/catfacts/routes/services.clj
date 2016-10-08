(ns catfacts.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [clj-http.client :as client]
            [catfacts.facts :refer [catfact]]))

(s/defschema CatFact
  {:response_type s/Str
   :text          s/Str})

(s/defschema CatGif
  {:response_type s/Str
   :attachments   [{:fallback  s/Str
                    :image_url s/Str}]})

(defapi service-routes
  {:swagger {:ui   "/swagger-ui"
             :spec "/swagger.json"
             :data {:info {:version     "1.0.0"
                           :title       "Sample API"
                           :description "Sample Services"}}}}

  (GET "/gif" []
    :return CatGif
    :summary "Gets a Cat Fact!"
    (ok {:response_type "in_channel"
         :attachments
         [{:fallback  "Cat Gif."
           :image_url (->
                        (client/get "http://thecatapi.com/api/images/get?format=src&type=gif"
                                    {:follow-redirects false})
                        :headers
                        (get "Location"))}]}))
  (GET "/fact" []
    :return CatFact
    :summary "Gets a Cat Fact!"
    (ok {:response_type "in_channel"
         :text          (catfact)})))
