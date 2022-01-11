(ns nopsteam.adapters
  (:require [byte-streams :as bs]
            [clojure.edn :as edn]
            [nopsteam.schemas :as s]))

(defn ->peer-request
  {:malli/schema [:=> [:cat :any] s/peer-request]}
  [request]
  (let [request-message (edn/read-string (bs/to-string (.getData request)))
        host-address (-> request .getAddress .getHostAddress)
        port (.getPort request)]
    {:id (str (-> request-message :sender :id)
              (-> request-message :receiver :id))
     :socket-address (.getSocketAddress request)
     :host-address host-address
     :port port
     :request request-message}))

(defn ->server-response-message
  {:malli/schema [:=> [:cat s/peer-request s/peer-request] s/message]}
  [sender receiver]
  {:type :server-response
   :sender {:id (-> sender :request :sender :id)
            :address {:ip (:host-address sender)
                      :port (:port sender)}}
   :receiver {:id (-> receiver :request :receiver :id)
              :address {:ip (:host-address receiver)
                        :port (:port receiver)}}})

(defn ->server-request-message
  {:malli/schema [:=> [:cat :keyword :keyword] s/message]}
  [sender-id receiver-id]
  {:type :server-request
   :sender {:id sender-id}
   :receiver {:id receiver-id}})
