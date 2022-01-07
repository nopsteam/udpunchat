(ns nopsteam.messaging
  (:require [byte-streams :as bs]
            [taoensso.timbre :as t])
  (:import [java.net DatagramPacket]))

(defn empty-message [n]
  (new DatagramPacket (byte-array n) n))

;; TODO: testar essa fita
(defn receive [message-size socket]
  (let [received-packet-hole (empty-message message-size)]
    (.receive socket received-packet-hole)
    (t/log :info {:packet-data (bs/to-string (.getData received-packet-hole))})
    received-packet-hole))

(defn new-packet
  ([message socket]
   (let [byte-message (bs/to-byte-array message)]
     (DatagramPacket. byte-message (count byte-message) socket)))
  ([message address port]
   (let [byte-message (bs/to-byte-array message)]
     (DatagramPacket. byte-message (count byte-message) address port))))
