(ns nopsteam.server
  (:require [byte-streams :as bs]
            [nopsteam.messaging :as m])
  (:import [java.net InetAddress InetSocketAddress DatagramSocket]))

(def udp-port 7070)

(defn future-receive [server client-id]
  (let [packet (m/empty-message 1024)]
    (println "Waiting for client:" client-id)
    (.receive server packet)
    (println "Received packet:" (bs/to-string (.getData packet)))
    packet))

(defn listen
  [_]
  (let [udp-server (DatagramSocket. udp-port)

        client-packet-1 (future (future-receive udp-server 1))
        client-packet-2 (future (future-receive udp-server 2))

        client-socket-1 (-> @client-packet-1 ^InetSocketAddress (.getSocketAddress))
        _debug-socket-1 (println "socket-address 1:" (.toString client-socket-1))
        client-address-1 (-> @client-packet-1 ^InetAddress (.getAddress))
        client-port-1 (-> @client-packet-1 ^int (.getPort))
        client-info-1 (str (.getHostAddress client-address-1) "-" client-port-1 "-")

        client-socket-2 (-> @client-packet-2 ^InetSocketAddress (.getSocketAddress))
        _debug-socket-2 (println "socket-address 2:" (.toString client-socket-2))
        client-address-2 (-> @client-packet-2 ^InetAddress (.getAddress))
        client-port-2 (-> @client-packet-2 ^int (.getPort))
        client-info-2 (str (.getHostAddress client-address-2) "-" client-port-2 "-")]

    (.send udp-server (m/new-packet client-info-2 client-socket-1))
    (.send udp-server (m/new-packet client-info-1 client-socket-2))

    (.close udp-server)))
