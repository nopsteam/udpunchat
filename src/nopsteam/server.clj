(ns nopsteam.server
  (:require [nopsteam.adapters :as a]
            [nopsteam.logics :as l]
            [nopsteam.messaging :as m]
            [taoensso.timbre :as t])
  (:import [java.net DatagramSocket]))

(def active-requests (atom []))

(def udp-port 7070)

(defn connect-peers [udp-server [client-1 client-2]]
  (.send udp-server (m/new-packet (a/client-info->str client-2) (:socket client-1)))
  (.send udp-server (m/new-packet (a/client-info->str client-1) (:socket client-2))))

(defn listen
  [_]
  (let [udp-server (DatagramSocket. udp-port)]
    (t/log :info "server started!")
    (loop []
      (let [client-info (-> (m/receive 1024 udp-server) a/request->client-info)]
        (when (l/request-not-exists? client-info @active-requests)
          (swap! active-requests conj client-info))

        (when (l/has-peers? @active-requests)
          (connect-peers udp-server @active-requests)
          (reset! active-requests [])))
      (recur))))
