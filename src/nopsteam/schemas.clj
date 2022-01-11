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

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(def JavaDatagramSocket
  (m/-simple-schema
   {:type :datagram-socket
    :pred (fn [socket] (instance? java.net.DatagramSocket socket))
    :type-properties {:error/message "should be an instance of java.net.DatagramSocket"
                      :gen/gen (gen/return (java.net.DatagramSocket.))}}))

(def JavaSocketAddress
  (m/-simple-schema
   {:type :socket-address
    :pred (fn [socket-address] (instance? java.net.SocketAddress socket-address))
    :type-properties {:error/message "should be an instance of java.net.SocketAddress"
                      :gen/gen (gen/return (.getLocalSocketAddress (java.net.DatagramSocket.)))}}))

(def peer-request
  [:map
   [:id :string]
   [:socket JavaSocketAddress]
   [:host-address :string]
   [:port :int]
   [:request message]])
