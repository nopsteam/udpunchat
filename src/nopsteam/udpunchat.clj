(ns nopsteam.udpunchat
  (:require [byte-streams :as bs]
            [clojure.string :as str])
  (:import [java.net InetAddress InetSocketAddress DatagramPacket DatagramSocket])
  (:gen-class))

(def udp-port 7070)

(defn new-packet
  ([message socket]
   (let [byte-message (bs/to-byte-array message)]
     (DatagramPacket. byte-message (count byte-message) socket)))
  ([message address port]
   (let [byte-message (bs/to-byte-array message)]
     (DatagramPacket. byte-message (count byte-message) address port))))

(defn empty-message [n]
  (new DatagramPacket (byte-array n) n))

(defn future-receive [server client-id]
  (let [packet (empty-message 1024)]
    (println "Waiting for client:" client-id)
    (.receive server packet)
    (println "Received packet:" (bs/to-string (.getData packet)))
    packet))

(defn server-loop
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

    (.send udp-server (new-packet client-info-2 client-socket-1))
    (.send udp-server (new-packet client-info-1 client-socket-2))

    (.close udp-server)))

(defn -main
  "Main server"
  [& args]
  (server-loop args))

(defn loop-receive
  [socket]
  (loop []
    (let [received-packet-hole (empty-message 1024)]
      (.receive socket received-packet-hole)
      (println (bs/to-string (.getData received-packet-hole))))
    (recur)))

(defn loop-send
  [client-id socket ip port]
  (loop [message (read-line)]
    (.send socket (new-packet (str client-id ": " message) ip port))
    (recur (read-line))))

(defn client-connect
  [{:keys [server-port server-ip client-id]}]
  (println "client:" client-id "connecting to:" server-ip server-port)
  (let [client-socket (DatagramSocket.)

        send-packet (new-packet (str "Hello:" client-id)
                                (InetAddress/getByName server-ip)
                                server-port)
        _send (.send client-socket send-packet)

        received-packet (empty-message 1024)
        _receive (.receive client-socket received-packet)

        [str-ip str-port] (str/split (bs/to-string (.getData received-packet)) #"-")
        ip (InetAddress/getByName str-ip)
        port (Long/parseLong  str-port)

        local-port (.getLocalPort client-socket)
        _close-socket (.close client-socket)
        client-socket-hole (DatagramSocket. local-port)]

    (println "IP:" ip "PORT:" port)

    (.send client-socket-hole (new-packet (str "Client Connected: " client-id) ip port))

    (future (loop-receive client-socket-hole))
    (loop-send client-id client-socket-hole ip port)

    (.close client-socket-hole)))

(comment
  (server-loop nil)
  (client-connect {:server-port 7070
                   :server-ip "127.0.0.1"
                   :client-id 1})
  (client-connect {:server-port 7070
                   :server-ip "127.0.0.1"
                   :client-id 2}))
