(ns nopsteam.udpunchat
  (:require [nopsteam.server :as server]
            [nopsteam.client :as client])
  (:gen-class))

(defn server-loop [args] (server/listen args))

(defn client-connect
  [{:keys [server-port server-ip client-id]}]
  (client/connect server-port server-ip client-id))

(defn -main
  "Main server"
  [& args]
  (server/listen args))

(comment
  (server-loop nil)
  (client-connect {:server-port 7070
                   :server-ip "127.0.0.1"
                   :client-id 1})
  (client-connect {:server-port 7070
                   :server-ip "127.0.0.1"
                   :client-id 2}))
