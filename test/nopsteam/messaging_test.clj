(ns nopsteam.messaging-test
  (:require [clojure.test :refer [deftest testing is]]
            [clojure.string :as str]
            [nopsteam.messaging :as m]
            [byte-streams :as bs])
  (:import [java.net InetSocketAddress]))

(deftest messaging
  (testing "creation of a packet with an empty message"
    (let [packet (m/empty-message 1024)]

      (testing "an empty message (filled with \0)"
        (is (every? #(= 0 %) (.getData packet))))

      (testing "with the specified length"
        (is 1024
            (count (.getData packet))))))

  (testing "creation of a new packet with a given content"
    (let [address "127.0.0.1"
          port 8080
          socket (InetSocketAddress. address port)]

      (testing "a new packet with a given content, address a port"
        (let [message "I'm a message"
              packet (m/new-packet message (.getAddress socket) port)]

          (testing "with the given message"
            (is (= message (-> packet
                               .getData
                               bs/to-string))))

          (testing "specified host"
            (is (= (str "/" address)
                   (-> packet
                       .getSocketAddress
                       .getAddress
                       .toString))))

          (testing "specified port"
            (is (= port
                   (-> packet
                       .getSocketAddress
                       .getPort))))

          (testing "with the specified length"
            (is (count message)
                (count (.getData packet))))))

      (testing "a new packet with a given content and a socket"
        (let [message "I'm a message"
              packet (m/new-packet message socket)]

          (testing "with the given message"
            (is (= message (-> packet
                               .getData
                               bs/to-string))))

          (testing "specified host"
            (is (= (str "/" address)
                   (-> packet
                       .getSocketAddress
                       .getAddress
                       .toString))))

          (testing "specified port"
            (is (= port
                   (-> packet
                       .getSocketAddress
                       .getPort))))

          (testing "with the specified length"
            (is (count message)
                (count (.getData packet)))))))))



