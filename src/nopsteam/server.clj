(ns nopsteam.server
  (:require [nopsteam.adapters :as a]
            [nopsteam.logics :as l]
            [nopsteam.messaging :as m]
            [nopsteam.schemas :as s]
            [taoensso.timbre :as t])
  (:import [java.net DatagramSocket]))

(def active-peer-requests (atom []))

(def udp-port 7070)

(defn connect-peers
  {:malli/schema [:=> [:cat s/peer-request s/peer-request] :nil]}
  [udp-server client-1 client-2]
  (.send udp-server
         (m/new-packet (str (a/->server-response-message client-1 client-2))
                       (:socket-address client-1)))
  (.send udp-server
         (m/new-packet (str (a/->server-response-message client-2 client-1))
                       (:socket-address client-2))))

(defn listen
  [_]
  (let [udp-server (DatagramSocket. udp-port)]
    (t/log :info "server started!")
    (loop []
      (let [sender-request (-> (m/receive 1024 udp-server) a/->peer-request)]

        (when (l/request-not-exists? sender-request @active-peer-requests)
          (swap! active-peer-requests conj sender-request))

        (when-let [receiver-request (l/get-peer-receiver sender-request @active-peer-requests)]
          (connect-peers udp-server sender-request receiver-request)
          (swap! active-peer-requests #(l/remove-peer-requests % sender-request receiver-request))))
      (recur))))
