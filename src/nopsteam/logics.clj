(ns nopsteam.logics)

;TODO add schema & unit test
(defn request-not-exists?
  [sender-request requests]
  (not (some #(= (:id %) (:id sender-request)) requests)))

;TODO add schema & unit test
(defn get-peer-receiver
  [sender-request requests]
  (let [request (-> sender-request :request)
        peer-request-id (str (-> request :receiver :id)
                             (-> request :sender :id))]
    (->> requests
         (filter #(= (:id %) peer-request-id))
         first)))

;(get-peer-receiver
 ;{:request {:receiver {:id :bertin} :sender {:id :delboni}}}
 ;[{:id ":bertin:delboni", :socket nil, :host-address "127.0.0.1", :port 39097, :request {:type :server-request, :sender {:id :bertin}, :receiver {:id :delboni}}}
  ;{:id ":delboni:bertin", :socket nil, :host-address "127.0.0.1", :port 48998, :request {:type :server-request, :sender {:id :delboni}, :receiver {:id :bertin}}}])

;TODO add schema & unit test
(defn remove-peer-requests
  [requests sender-request peer-request]
  (remove #(or (= (:id %) (:id peer-request))
               (= (:id %) (:id sender-request))) requests))

;(remove-peer-requests
 ;[{:id ":delboni:bertin", :socket nil, :host-address "127.0.0.1", :port 35587, :request {:type :server-request, :sender {:id :delboni}, :receiver {:id :bertin}}}
  ;{:id ":bertin:delboni", :socket nil, :host-address "127.0.0.1", :port 50515, :request {:type :server-request, :sender {:id :bertin}, :receiver {:id :delboni}}}]
 ;{:id ":delboni:bertin"}
 ;{:id ":bertin:delboni"})
