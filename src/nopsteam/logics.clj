(ns nopsteam.logics
  (:require [nopsteam.schemas :as s]))

(defn request-not-exists?
  {:malli/schema [:=> [:cat s/peer-request [:vector s/peer-request]] :boolean]}
  [sender-request requests]
  (not (some #(= (:id %) (:id sender-request)) requests)))

(defn get-peer-receiver
  {:malli/schema [:=> [:cat s/peer-request [:vector s/peer-request]] [:maybe s/peer-request]]}
  [sender-request requests]
  (let [request (-> sender-request :request)
        peer-request-id (str (-> request :receiver :id)
                             (-> request :sender :id))]
    (->> requests
         (filter #(= (:id %) peer-request-id))
         first)))

(defn remove-peer-requests
  {:malli/schema [:=> [:cat [:vector s/peer-request] s/peer-request s/peer-request] [:vector s/peer-request]]}
  [requests sender-request peer-request]
  (remove #(or (= (:id %) (:id peer-request))
               (= (:id %) (:id sender-request))) requests))
