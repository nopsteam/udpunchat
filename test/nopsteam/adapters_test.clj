(ns nopsteam.adapters-test
  (:require [byte-streams :as bs]
            [clojure.test :refer [use-fixtures]]
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
    (m/validate s/peer-request (a/->peer-request packet))))
