(ns onyx-dragon.core
  (:use [amazonica.core]
        [amazonica.aws.cloudfront]
        [environ.core])
  (:require [clj-campfire.core :as campfire]))

(def sleep-time (or (env :sleep-time) 30))

(def aws-creds {:access-key (env :aws-key)
                :secret-key (env :aws-sec)
                :endpoint "eu-west-1"})

(def campfire-creds {:api-token (env :campfire-token)
                     :ssl true
                     :sub-domain (env :campfire-subdomain)})
(def room
  (env :campfire-room))

(defn report
  [in-flight]
  (let [message (if in-flight
                  "There are cache invalidations in-flight atm"
                  "All cache invalidations have completed!")]
    (println message)
    (when room
      (campfire/message campfire-creds room message))))

(defn in-flight-invalidations
  [distribution-id]
  (not-empty (filter #(= (:status %) "InProgress") (list-invalidations aws-creds :distribution-id distribution-id))))

(defn check-forever
  [distribution-id]
  (loop [in-flight (in-flight-invalidations distribution-id)]
    ;; When starting up and there are invalidations in flight, ensure to report that
    (when in-flight
      (report in-flight))
    (let [in-flight' (in-flight-invalidations distribution-id)]
      (if (not (= in-flight in-flight'))
        (report in-flight'))
      (Thread/sleep (* sleep-time 1000))
      (recur in-flight'))))

(defn -main
  []
  (let [distribution-id (env :distribution-id)]
    (check-forever distribution-id)))
