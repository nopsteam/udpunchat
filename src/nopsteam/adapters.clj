(ns nopsteam.adapters
  (:require [nopsteam.schemas :as s]))

(defn request->client-info
  [request]
  (let [host-address (-> request .getAddress .getHostAddress)
        port (.getPort request)]
    {:id (str host-address ":" port)
     :socket (.getSocketAddress request)
     :host-address host-address
     :port port}))

(defn client-info->str
  [{:keys [host-address port]}]
  (str host-address "-" port "-"))

(defn ->server-request-message 
  {:malli/schema [:=> [:cat :keyword :keyword] 
                  s/message]}
  [sender-id receiver-id]
  {:type :server-request
   :sender {:id sender-id}
   :receiver {:id receiver-id}})
