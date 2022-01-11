(ns nopsteam.schemas
  (:require [malli.core :as m]))

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

(def Socket
  (m/-simple-schema
   {:type :socket
    :pred (fn [socket] (instance? java.net.DatagramSocket socket))
    :type-properties {:error/message "should be an instance of java.net.DatagramSocket"}}))

(def peer-request
  [:map
   [:id :string]
   [:socket Socket]
   [:host-address :string]
   [:port :int]
   [:request message]])
