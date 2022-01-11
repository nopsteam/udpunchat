(ns nopsteam.schemas
  (:require [clojure.test.check.generators :as gen]
            [malli.core :as m]))

(def net-address
  [:map
   [:ip :string]
   [:port :int]])

(def peer
  [:map
   [:id :keyword]
   [:address {:optional true} net-address]])

(def message
  [:map
   [:type [:enum
           :server-request
           :server-response
           :client-handshake
           :message
           :message-relayed
           :disconnect]]
   [:server-address {:optional true} net-address]
   [:sender peer]
   [:receiver peer]
   [:payload {:optional true} :string]])

(def JavaDatagramSocket
  (m/-simple-schema
   {:type :datagram-socket
    :pred (fn [socket] (instance? java.net.DatagramSocket socket))
    :type-properties {:error/message "should be an instance of java.net.DatagramSocket"
                      :gen/gen (gen/return (java.net.DatagramSocket.))}}))

(def JavaDatagramPacket
  (m/-simple-schema
   {:type :datagram-packet
    :pred (fn [packet] (instance? java.net.DatagramPacket packet))
    :type-properties {:error/message "should be an instance of java.net.DatagramPacket"
                      :gen/gen (gen/fmap #(new java.net.DatagramPacket (byte-array %) %) gen/nat)}}))

(def JavaInetAddress
  (m/-simple-schema
   {:type :inet-address
    :pred (fn [socket-address] (instance? java.net.InetAddress socket-address))
    :type-properties {:error/message "should be an instance of java.net.InetAddress"
                      :gen/gen (gen/return (java.net.InetAddress/getByName "0.0.0.0"))}}))

(def JavaSocketAddress
  (m/-simple-schema
   {:type :socket-address
    :pred (fn [socket-address] (instance? java.net.SocketAddress socket-address))
    :type-properties {:error/message "should be an instance of java.net.SocketAddress"
                      :gen/gen (gen/return (.getLocalSocketAddress (java.net.DatagramSocket.)))}}))

(def peer-request
  [:map
   [:id :string]
   [:socket-address JavaSocketAddress]
   [:host-address :string]
   [:port :int]
   [:request message]])
