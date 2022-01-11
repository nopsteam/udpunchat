(ns nopsteam.logics-test
  (:require [clojure.test :refer [deftest is testing]]
            [malli.generator :as mg]
            [nopsteam.logics :as l]
            [nopsteam.schemas :as s])
  (:import [java.net DatagramSocket]))

(def sender-request-1
  {:id ":bertin:delboni"
   :socket-address (.getLocalSocketAddress (DatagramSocket.))
   :host-address "127.0.0.1"
   :port 12345
   :request {:type :server-request
             :sender {:id :bertin}
             :receiver {:id :delboni}}})

(def sender-request-2
  {:id ":delboni:bertin"
   :socket-address (.getLocalSocketAddress (DatagramSocket.))
   :host-address "127.0.0.1"
   :port 35587
   :request {:type :server-request
             :sender {:id :delboni}
             :receiver {:id :bertin}}})

(deftest request-not-exists-test
  (testing "should not exists on empty requests"
    (is (true? (l/request-not-exists? sender-request-1 []))))
  (testing "should not exists on not empty requests"
    (is (true? (l/request-not-exists? sender-request-1 [sender-request-2]))))
  (testing "should exists on empty requests"
    (is (false? (l/request-not-exists? sender-request-1 [sender-request-1 sender-request-2])))))

(deftest get-peer-receiver-test
  (testing "should not find receiver"
    (is (nil? (l/get-peer-receiver sender-request-1 [])))
    (is (nil? (l/get-peer-receiver sender-request-1 [sender-request-1]))))
  (testing "should find receiver request"
    (is (= sender-request-2
           (l/get-peer-receiver sender-request-1 [sender-request-2])))
    (is (= sender-request-2
           (l/get-peer-receiver sender-request-1 [sender-request-1 sender-request-2])))
    (is (= sender-request-1
           (l/get-peer-receiver sender-request-2 [sender-request-1 sender-request-2])))))

(deftest remove-peer-requests-test
  (testing "should return empty vector"
    (is (= []
           (l/remove-peer-requests [] sender-request-1 sender-request-2)))
    (is (= []
           (l/remove-peer-requests [sender-request-1 sender-request-2] sender-request-1 sender-request-2)))
    (is (= []
           (l/remove-peer-requests [sender-request-1 sender-request-2] sender-request-2 sender-request-1)))
    (testing "should return vector with the other requests"
      (let [random-request-1 (mg/generate s/peer-request)
            random-request-2 (mg/generate s/peer-request)]
        (is (= [random-request-1 random-request-2]
               (l/remove-peer-requests [random-request-1 random-request-2 sender-request-1 sender-request-2]
                                       sender-request-1 sender-request-2)))))))
