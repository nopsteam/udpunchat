(ns nopsteam.messaging
  (:require [byte-streams :as bs])
  (:import [java.net DatagramPacket]))

(defn empty-message [n]
  (new DatagramPacket (byte-array n) n))

(defn new-packet
  ([message socket]
   (let [byte-message (bs/to-byte-array message)]
     (DatagramPacket. byte-message (count byte-message) socket)))
  ([message address port]
   (let [byte-message (bs/to-byte-array message)]
     (DatagramPacket. byte-message (count byte-message) address port))))
