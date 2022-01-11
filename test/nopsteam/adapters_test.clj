(ns nopsteam.adapters-test
  (:require [byte-streams :as bs]
            [clojure.test :refer [use-fixtures is]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [malli.core :as m]
            [malli.generator :as mg]
            [nopsteam.adapters :as a]
            [nopsteam.schemas :as s]
            [nopsteam.utils :as u])
  (:import [java.net DatagramPacket DatagramSocket]))

(use-fixtures :once u/with-malli-intrumentation)

(defspec ->peer-request-generator
  100
  (prop/for-all [packet (gen/fmap (fn [message]
                                    (let [byte-message (bs/to-byte-array (str message))]
                                      (new DatagramPacket
                                           byte-message
                                           (count byte-message)
                                           (.getLocalSocketAddress (DatagramSocket.)))))
                                  (mg/generator s/message))]
    (is (nil? (m/explain s/peer-request (a/->peer-request packet))))))

(defspec ->server-response-message-generator
  100
  (prop/for-all [sender-peer-request (mg/generator s/peer-request)
                 receiver-peer-request (mg/generator s/peer-request)]
    (is (nil? (m/explain s/message (a/->server-response-message sender-peer-request receiver-peer-request))))))

(defspec ->server-request-message-generator
  100
  (prop/for-all [sender-id (mg/generator :keyword)
                 receiver-id (mg/generator :keyword)]
    (is (nil? (m/explain s/message (a/->server-request-message sender-id receiver-id))))))
