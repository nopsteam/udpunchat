(ns nopsteam.utils
  (:require [malli.instrument :as mi]))

(defn with-malli-intrumentation
  "Wraps f ensuring there is an embedded Kafka before running it"
  [f]
  (mi/collect!)
  (mi/instrument!)
  (f)
  (mi/unstrument!))
