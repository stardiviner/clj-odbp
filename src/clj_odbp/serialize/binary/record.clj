(ns clj-odbp.serialize.binary.record
  (:require [clj-odbp.serialize.binary.varint :as v])
  (:import [java.io ByteArrayOutputStream DataOutputStream]
           [java.lang Float]
           [java.text SimpleDateFormat]))

(defprotocol Serialization
  (serialize [value]))

(defprotocol OrientType
  (oserialize [this]))

(defn serialize-by-type [value]
  (if (satisfies? OrientType value)
    (.oserialize value)
    (serialize value)))

(defn short-type
  [value]
  (v/varint value))

(extend-type java.lang.Short
  Serialization
  (serialize [value]
    (short-type value)))

(defn integer-type
  [value]
  (v/varint value))

(extend-type java.lang.Integer
  Serialization
  (serialize [value]
    (integer-type value)))

(defn long-type
  [value]
  (v/varint value))

(extend-type java.lang.Long
  Serialization
  (serialize [value]
    (long-type value)))

(defn byte-type
  [value]
  value)

(extend-type java.lang.Byte
  Serialization
  (serialize [value]
    (byte-type value)))

(defn boolean-type
  [value]
  (if value
    (byte 1)
    (byte 0)))

(extend-type java.lang.Boolean
  Serialization
  (serialize [value]
    (boolean-type value)))

(defn float-type
  [value]
  (let [bos (ByteArrayOutputStream. 4)
        dos (DataOutputStream. bos)]
    (.writeFloat dos value)
    (.toByteArray bos)))

(extend-type java.lang.Float
  Serialization
  (serialize [value]
    (float-type value)))

(defn double-type
  [value]
  (let [bos (ByteArrayOutputStream. 8)
        dos (DataOutputStream. bos)]
    (.writeDouble dos value)
    (.toByteArray bos)))

(extend-type java.lang.Double
  Serialization
  (serialize [value]
    (double-type value)))

(defn bytes-type
  [value]
  (let [bos (ByteArrayOutputStream.)
        dos (DataOutputStream. bos)
        size (count value)
        size-varint (byte-array (integer-type size))
        size-varint-len (count size-varint)]
    (.write dos size-varint 0 size-varint-len)
    (.write dos value 0 size)
    (.toByteArray bos)))

(defn string-type
  [value]
  (let [bytes (.getBytes value)]
    (bytes-type bytes)))

(extend-type java.lang.String
  Serialization
  (serialize [value]
    (string-type value)))

(defn keyword-type
  [value]
  (string-type (name value)))

(extend-type clojure.lang.Keyword
  Serialization
  (serialize [value]
    (keyword-type value)))

(defn coll-type
  [value]
  (map serialize-by-type value))

(defn map-type
  [value]
  (->> value
       vec
       flatten
       (map serialize-by-type)))

(deftype OrientBinary [value]
  OrientType
  (oserialize [this]
    (bytes-type value)))

(defn orient-binary [value]
  (->OrientBinary value))

(deftype OrientDate [value]
  OrientType
  (oserialize [this]
    (let [formatter (SimpleDateFormat. "dd/MM/yyyy")
          date (.value this)
          date-without-time (.parse formatter (.format formatter date))
          date->long (.getTime date-without-time)]
      (long-type (long (/ date->long 86400))))))

(defn orient-date [value]
  (->OrientDate value))

(deftype OrientDateTime [value]
  OrientType
  (oserialize [this]
    (long-type (.getTime (.value this)))))

(defn orient-date-time [value]
  (->OrientDateTime value))

(defn serialize-header [])

(defn serialize-data [])

(defn serialize-record [record]
  (serialize-header)
  (serialize-data))
