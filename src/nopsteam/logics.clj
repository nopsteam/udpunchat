(ns nopsteam.logics
  (:require [nopsteam.schemas :as s]))

;TODO unit test
(defn request-not-exists?
  {:malli/schema [:=> [:cat s/peer-request [:vector s/peer-request]] :boolean]}
  [sender-request requests]
  (not (some #(= (:id %) (:id sender-request)) requests)))

;(request-not-exists?
 ;{:id ":bertin:delboni" :socket (java.net.DatagramSocket.) :host-address "127.0.0.1" :port 12345
  ;:request {:type :server-request
            ;:receiver {:id :bertin}
            ;:sender {:id :delboni}}}
 ;[{:id ":bertin:delboni", :socket (java.net.DatagramSocket.), :host-address "127.0.0.1", :port 39097, :request {:type :server-request, :sender {:id :bertin}, :receiver {:id :delboni}}}
  ;{:id ":delboni:bertin", :socket (java.net.DatagramSocket.), :host-address "127.0.0.1", :port 48998, :request {:type :server-request, :sender {:id :delboni}, :receiver {:id :bertin}}}])

;TODO unit test
(defn get-peer-receiver
  {:malli/schema [:=> [:cat s/peer-request [:vector s/peer-request]] s/peer-request]}
  [sender-request requests]
  (let [request (-> sender-request :request)
        peer-request-id (str (-> request :receiver :id)
                             (-> request :sender :id))]
    (->> requests
         (filter #(= (:id %) peer-request-id))
         first)))

;(get-peer-receiver
 ;{:id ":bertin:delboni" :socket (java.net.DatagramSocket.) :host-address "127.0.0.1" :port 12345
  ;:request {:type :server-request
            ;:receiver {:id :bertin}
            ;:sender {:id :delboni}}}
 ;[{:id ":bertin:delboni", :socket (java.net.DatagramSocket.), :host-address "127.0.0.1", :port 39097, :request {:type :server-request, :sender {:id :bertin}, :receiver {:id :delboni}}}
  ;{:id ":delboni:bertin", :socket (java.net.DatagramSocket.), :host-address "127.0.0.1", :port 48998, :request {:type :server-request, :sender {:id :delboni}, :receiver {:id :bertin}}}])

;TODO unit test
(defn remove-peer-requests
  {:malli/schema [:=> [:cat [:vector s/peer-request] s/peer-request s/peer-request] [:vector s/peer-request]]}
  [requests sender-request peer-request]
  (remove #(or (= (:id %) (:id peer-request))
               (= (:id %) (:id sender-request))) requests))

;(remove-peer-requests
 ;[{:id ":delboni:bertin", :socket (java.net.DatagramSocket.), :host-address "127.0.0.1", :port 35587,
   ;:request {:type :server-request, :sender {:id :delboni}, :receiver {:id :bertin}}}
  ;{:id ":bertin:delboni", :socket (java.net.DatagramSocket.), :host-address "127.0.0.1", :port 50515,
   ;:request {:type :server-request, :sender {:id :bertin}, :receiver {:id :delboni}}}]
 ;{:id ":bertin:delboni" :socket (java.net.DatagramSocket.) :host-address "127.0.0.1" :port 12345
  ;:request {:type :server-request
            ;:receiver {:id :bertin}
            ;:sender {:id :delboni}}}
 ;{:id ":delboni:bertin" :socket (java.net.DatagramSocket.) :host-address "127.0.0.1" :port 54321
  ;:request {:type :server-request
            ;:receiver {:id :delboni}
            ;:sender {:id :bertin}}})
