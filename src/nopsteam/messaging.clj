(ns nopsteam.messaging
  (:require [byte-streams :as bs]
            [nopsteam.schemas :as s]
            [taoensso.timbre :as t])
  (:import [java.net DatagramPacket]))

(defn empty-message
  {:malli/schema [:=> [:cat :int] s/JavaDatagramPacket]}
  [n]
  (new DatagramPacket (byte-array n) n))

;; TODO: testar essa fita
(defn receive
  {:malli/schema [:=> [:cat :int s/JavaDatagramSocket] s/JavaDatagramPacket]}
  [message-size socket]
  (let [received-packet-hole (empty-message message-size)]
    (.receive socket received-packet-hole)
    (t/log :info {:packet-data (bs/to-string (.getData received-packet-hole))})
    received-packet-hole))

(defn new-packet
  {:malli/schema [:function
                  [:=> [:cat :string s/JavaSocketAddress] s/JavaDatagramPacket]
                  [:=> [:cat :string s/JavaInetAddress :int] s/JavaDatagramPacket]]}
  ([message socket]
   (let [byte-message (bs/to-byte-array message)]
     (DatagramPacket. byte-message (count byte-message) socket)))
  ([message address port]
   (let [byte-message (bs/to-byte-array message)]
     (DatagramPacket. byte-message (count byte-message) address port))))
