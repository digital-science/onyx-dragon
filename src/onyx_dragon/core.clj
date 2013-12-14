(ns onyx-dragon.core
  (:use
    [environ.core])
  (:require
    [amazonica.core :as aws]
    [amazonica.aws.cloudfront :as cloudfront]
    [clj-campfire.core :as campfire]
    [hipchat.core :as hipchat]))

(def sleep-time
  (or (env :sleep-time)
      30))

(def aws-creds
  {:access-key (env :aws-key)
   :secret-key (env :aws-sec)
   :endpoint "eu-west-1"})

(def campfire-creds
  {:api-token (env :campfire-token)
   :ssl true
   :sub-domain (env :campfire-subdomain)})

(def room
  (env :campfire-room))

(when (env :hipchat-token)
  (hipchat/set-auth-token! (env :hipchat-token)))

(def hipchat-room
  (env :hipchat-room-id))

(defn report
  [in-flight]
  (let [message (if in-flight
                  "There are cache invalidations in-flight atm"
                  "All cache invalidations have completed!")]
    (println message)
    (when room
      (campfire/message campfire-creds room message))
    (when hipchat-room
      (hipchat/send-message-to-room hipchat-room message :color (if in-flight
                                                                  "red"
                                                                  "green")))))

(defn all-invalidations
  [distribution-id]
  (-> (cloudfront/list-invalidations aws-creds :distribution-id distribution-id)
      :invalidation-list
      :items))

(defn in-flight-invalidations
  [distribution-id]
  (not-empty (filter #(= (:status %) "InProgress")
                     (all-invalidations distribution-id))))

(defn check-forever
  [distribution-id]
  (let [in-flight (in-flight-invalidations distribution-id)]
    ;; When starting up and there are invalidations in flight,
    ;; ensure to report that
    (when in-flight
      (report in-flight))
    (loop [in-flight in-flight]
      (let [in-flight' (in-flight-invalidations distribution-id)]
        (if (not (= in-flight in-flight'))
          (report in-flight'))
        (Thread/sleep (* sleep-time 1000))
        (recur in-flight')))))

(defn -main
  []
  (let [distribution-id (env :distribution-id)]
    (check-forever distribution-id)))
