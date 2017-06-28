(ns clj-odbp.deserialize
  (import [java.io DataInputStream]))

(defn bool-type
  "Read a byte from the input stream and return a boolean. (1=true, 0=false)"
  [^DataInputStream in]
  (let [value (.readByte in)]
    (= 0x1 value)))

(defn byte-type
  "Read a single byte from the input stream."
  [^DataInputStream in]
  (.readByte in))

(defn short-type
  "Read a Short from the input stream." 
  [^DataInputStream in]
  (.readShort in))

(defn int-type
  "Read an integer from the input stream."
  [^DataInputStream in]
  (.readInt in))

(defn long-type
  "Read a Long from the input stream."
  [^DataInputStream in]
  (.readLong in))

(defn bytes-type
  "Read a sequence of bytes from the input stream."
  [^DataInputStream in]
  (let [len (int-type in)
        buffer (byte-array len)] 
    (.read in buffer 0 len)
    (vec buffer)))

(defn string-type
  "Read a string from the input stream."
  [^DataInputStream in]
  (let [len (int-type in)
        buffer (byte-array len)]
    (.read in buffer 0 len)
    (apply str (map char buffer))))

(defn strings-type
  "Read a set of strings from the input stream."
  [^DataInputStream in]
  (let [n (int-type in)]
    (vec (repeat n (string-type in)))))

(defn decode
  [^DataInputStream in spec] 
  (reduce-kv
   (fn [result key f] 
     (assoc result key (f in)))
   {}
   spec))
