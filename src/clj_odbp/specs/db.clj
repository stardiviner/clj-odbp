(ns clj-odbp.specs.db
  (require
   [clj-odbp.serialize :as s]
   [clj-odbp.deserialize :as d]))

;; REQUEST_CONNECT
(def connect-request
  {:operation s/byte-type
   :session s/int-type
   :driver-name s/string-type
   :driver-version s/string-type
   :protocol-version s/short-type
   :client-id s/string-type
   :serialization s/string-type
   :token-session s/bool-type
   :support-push s/bool-type
   :collect-stats s/bool-type
   :username s/string-type
   :password s/string-type})

(def connect-response
  {:response-session d/int-type
   :session-id d/int-type
   :token d/bytes-type})

;; REQUEST_DB_OPEN
(def connect-db-request
  {:operation s/byte-type
   :session s/int-type
   :driver-name s/string-type
   :driver-version s/string-type
   :protocol-version s/short-type
   :client-id s/string-type
   :serialization s/string-type
   :token-session s/bool-type
   :support-push s/bool-type
   :collect-stats s/bool-type
   :database-name s/string-type
   :username s/string-type
   :password s/string-type})

(def connect-db-response
  {:response-session d/int-type
   :session-id d/int-type
   :token d/bytes-type
   :clusters (d/array-of [d/string-type d/short-type])
   :cluster-config d/bytes-type
   :orient-db-relase d/string-type})

;; REQUEST_SHUTDOWN
(def shutdown-request
  {:operation s/byte-type
   :session-id s/int-type
   :username s/string-type
   :password s/string-type})

;; REQUEST_DB_CLOSE
(def db-close-request
  {:operation s/byte-type})

;; REQUEST_DB_EXIST
(def db-exist-request
  {:operation s/byte-type
   :session-id s/int-type
   :database-name s/string-type
   :server-storage-type s/string-type})

(def db-exist-response
  {:session-id d/int-type
   :result d/bool-type})

;; REQUEST_DB_DROP
(def db-drop-request
  {:operation s/byte-type
   :session-id s/int-type
   :database-name s/string-type
   :storage-type s/string-type})

;; REQUEST_DB_SIZE
(def db-size-request
  {:operation s/byte-type
   :session-id s/int-type})

(def db-size-response
  {:session-id d/int-type
   :size d/long-type})

;; REQUEST_DB_COUNTRECORDS
(def db-countrecords-request
  {:operation s/byte-type
   :session-id s/int-type})

(def db-countrecords-response
  {:session-id d/int-type
   :count d/long-type})

;; REQUEST_DB_RELOAD
(def db-reload-request
  {:operation s/byte-type
   :session-id s/int-type})

(def db-reload-response
  {:session-id d/int-type
   :clusters (d/array-of [d/string-type d/short-type])})
