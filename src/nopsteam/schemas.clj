(ns nopsteam.schemas)

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
           :client-handshake
           :message
           :message-relayed
           :disconnect]]
   [:server-address {:optional true} net-address]
   [:sender peer]
   [:receiver peer]
   [:payload {:optional true} :string]])
