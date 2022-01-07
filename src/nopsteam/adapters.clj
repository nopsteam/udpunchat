(ns nopsteam.adapters)

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
