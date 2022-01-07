(ns nopsteam.client
  (:require [byte-streams :as bs]
            [clojure.string :as str]
            [nopsteam.messaging :as m])
  (:import [java.net InetAddress DatagramSocket]))

(defn loop-send
  [client-id socket ip port]
  (loop [message (read-line)]
    (.send socket (m/new-packet (str client-id ": " message) ip port))
    (recur (read-line))))

(defn loop-receive
  [socket]
  (loop []
    (let [received-packet-hole (m/empty-message 1024)]
      (.receive socket received-packet-hole)
      (println (bs/to-string (.getData received-packet-hole))))
    (recur)))

(defn connect
  [server-port server-ip client-id]
  (println "client:" client-id "connecting to:" server-ip server-port)
  (let [client-socket (DatagramSocket.)

        send-packet (m/new-packet (str "Hello:" client-id)
                                  (InetAddress/getByName server-ip)
                                  server-port)
        _send (.send client-socket send-packet)

        received-packet (m/empty-message 1024)
        _receive (.receive client-socket received-packet)

        [str-ip str-port] (str/split (bs/to-string (.getData received-packet)) #"-")
        ip (InetAddress/getByName str-ip)
        port (Long/parseLong  str-port)

        local-port (.getLocalPort client-socket)
        _close-socket (.close client-socket)
        client-socket-hole (DatagramSocket. local-port)]

    (println "IP:" ip "PORT:" port)

    (.send client-socket-hole (m/new-packet (str "Client Connected: " client-id) ip port))

    (future (loop-receive client-socket-hole))
    (loop-send client-id client-socket-hole ip port)

    (.close client-socket-hole)))
