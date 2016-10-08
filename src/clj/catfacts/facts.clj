(ns catfacts.facts
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as s]
            [mount.core :refer [defstate]]
            [catfacts.config :refer [env]]))

(defn load-facts []
  (let [facts (-> (or
                    (:facts env)
                    (io/resource "facts.edn"))
                  (slurp)
                  (edn/read-string))]
    {:facts facts
     :fact-count (count facts)}))

(defstate facts :start (load-facts))

(defn asshole-fact [fact]
  (if (> (rand) 0.85)
    (-> fact
        (s/replace #"Cat" "Asshole")
        (s/replace #"cat" "asshole"))
    fact))

(defn catfact []
  (let [idx (rand-int (:fact-count facts))
        fact (get-in facts [:facts idx])]
    (str "Cat Fact " (inc idx) ": " (asshole-fact fact) "\n:cat: :cat: :cat:")))
