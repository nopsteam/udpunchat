(ns nopsteam.logics)

(defn request-not-exists? [client-info requests]
  (not (some #(= (:hostaddress %) (:host-address client-info)) requests)))

(defn has-peers? [requests]
  (= 2 (count requests)))

