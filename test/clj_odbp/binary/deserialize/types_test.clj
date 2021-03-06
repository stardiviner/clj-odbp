;; Copyright 2017 7bridges s.r.l.
;;
;; Licensed under the Apache License, Version 2.0 (the "License");
;; you may not use this file except in compliance with the License.
;; You may obtain a copy of the License at
;;
;; http://www.apache.org/licenses/LICENSE-2.0
;;
;; Unless required by applicable law or agreed to in writing, software
;; distributed under the License is distributed on an "AS IS" BASIS,
;; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;; See the License for the specific language governing permissions and
;; limitations under the License.

(ns clj-odbp.binary.deserialize.types-test
  (:require [clj-odbp.binary.deserialize.types :as t]
            [clj-odbp.binary.deserialize.buffer :as b]
            [midje.sweet :refer :all]))

(facts "Testing basic types."
       (fact "Bool - 0 is false."
             (t/call :bool-orient-type
                     (b/to-buffer [0])) => false)
       (fact "Bool - 1 is true."
             (t/call :bool-orient-type
                     (b/to-buffer [1])) => true)
       (fact "Integer - [0] is 0."
             (t/call :integer-orient-type
                     (b/to-buffer [0])) => (int 0))
       (fact "Integer - [1] is -1."
             (t/call :integer-orient-type
                     (b/to-buffer [1]) 0) => (int -1))
       (fact "Integer - [10] is 5."
             (t/call :integer-orient-type
                     (b/to-buffer [10]) 0) => 5)
       (fact "Integer - [172 2] is 150."
             (t/call :integer-orient-type
                     (b/to-buffer [172 2]) 0) => 150)
       (fact "Short - [0] is 0."
             (t/call :short-orient-type
                     (b/to-buffer [0]) 0) => (int 0))
       (fact "Short - [1] is -1."
             (t/call :short-orient-type
                     (b/to-buffer [1]) 0) => (int -1))
       (fact "Short - [10] is 5."
             (t/call :short-orient-type
                     (b/to-buffer [10]) 0) => 5)
       (fact "Short - [172 2] is 150."
             (t/call :short-orient-type
                     (b/to-buffer [172 2]) 0) => 150)
       (fact "Long - [0] is 0."
             (t/call :long-orient-type
                     (b/to-buffer [0]) 0) => (int 0))
       (fact "Long - [1] is -1."
             (t/call :long-orient-type
                     (b/to-buffer [1]) 0) => (int -1))
       (fact "Long - [10] is 5."
             (t/call :long-orient-type
                     (b/to-buffer [10]) 0) => 5)
       (fact "Long - [172 2] is 150."
             (t/call :long-orient-type
                     (b/to-buffer [172 2]) 0) => 150)
       (fact "Float - [0] is 0."
             (t/call :float-orient-type
                     (b/to-buffer [0 0 0 0]) 0) => (float 0.0))
       (fact "Float - [-65 -128 0 0] is -1."
             (t/call :float-orient-type
                     (b/to-buffer [-65 -128 0 0]) 0) => (float -1.0))
       (fact "Float - [64 -96 0 0] is 5."
             (t/call :float-orient-type
                     (b/to-buffer [64 -96 0 0]) 0) => (float 5.0))
       (fact "Float - [67 22 25 -102] is 150.1"
             (t/call :float-orient-type
                     (b/to-buffer [67 22 25 -102]) 0) => (float 150.1))
       (fact "Double - [0] is 0."
             (t/call :double-orient-type
                     (b/to-buffer [0 0 0 0
                                   0 0 0 0]) 0) => (double 0.0))
       (fact "Double - [-65 -16 0 0 0 0 0 0] is -1."
             (t/call :double-orient-type
                     (b/to-buffer [-65 -16 0 0
                                   0 0 0 0]) 0) => (double -1.0))
       (fact "Double - [64 20 0 0 0 0 0 0] is 5."
             (t/call :double-orient-type
                     (b/to-buffer [64 20 0 0
                                   0 0 0 0]) 0) => (double 5.0))
       (fact "Double - [64 98 -61 -41 10 61 112 -92] is 150.1"
             (t/call :double-orient-type
                     (b/to-buffer [64 98 -61 -41
                                   10 61 112 -92]) 0) => (double 150.12))
       (fact "Datetime - [0] is 1970-01-01T00:00:00"
             (t/call :datetime-orient-type
                     (b/to-buffer [0]) 0) => (java.util.Date. 0))
       (fact "Datetime - [206 130 225 133 176 87] is 2017-07-26T19:25:07"
             (t/call :datetime-orient-type
                     (b/to-buffer [206 130 225
                                   133 176 87]) 0) => (java.util.Date. 1501097107623))
       (fact "String - Empty string"
             (t/call :string-orient-type
                     (b/to-buffer [0]) 0) => "")
       (fact "String - Simple string"
             (t/call :string-orient-type
                     (b/to-buffer [10 77 97 114 99 111]) 0) => "Marco")
       (fact "Binary - Empty raw bytes"
             (t/call :binary-orient-type
                     (b/to-buffer [0]) 0) => {:_obinary {:value []}})
       (fact "Binary - eight raw bytes"
             (t/call :binary-orient-type
                     (b/to-buffer [16
                                   0 1 2 3
                                   4 5 6 7]) 0) => {:_obinary {:value [0 1 2 3
                                                                       4 5 6 7]}})
       (fact "EmbeddedList - 2 Integers"
             (t/call :embedded-list-orient-type
                     (b/to-buffer [4 1 2 4])) => [1 2])
       (fact "EmbeddedList - two Strings"
             (t/call :embedded-list-orient-type
                     (b/to-buffer [4 7
                                   4 97 98
                                   4 99 100])) => ["ab" "cd"])
       (fact "EmbeddedSet - three Integers (+ one duplicate)"
             (t/call :embedded-set-orient-type
                     (b/to-buffer [8 1 2 4 2 6])) => (just 1 2 3))
       (fact "EmbeddedSet - three Strings (+ one duplicate)"
             (t/call :embedded-set-orient-type
                     (b/to-buffer [6 7
                                   4 97 98
                                   4 99 100
                                   4 97 98])) => #{"ab" "cd"})
       (fact "Link - one ORid"
             (t/call :link-orient-type
                     (b/to-buffer [4 6])) => "#2:3")
       (fact "Link list - three ORid"
             (t/call :link-list-orient-type
                     (b/to-buffer [6
                                   4 6
                                   8 10
                                   12 14])) => ["#2:3" "#4:5" "#6:7"])
       (fact "Link set - three ORid"
             (t/call :link-list-orient-type
                     (b/to-buffer [6
                                   4 6
                                   8 10
                                   12 14])) => ["#2:3" "#4:5" "#6:7"])
       (fact "Link map - three ORid"
             (t/call :link-map-orient-type
                     (b/to-buffer [6
                                   1 2 4 6
                                   7 4 97 98 8 10
                                   3 6 12 14])) => {1 "#2:3"
                                                    "ab" "#4:5"
                                                    3 "#6:7"})
       (fact "ORidBag - Embedded ORid Bag"
             (t/call :link-bag-orient-type
                     (b/to-buffer [3
                                   0 0 0 0 0 0 0 1
                                   0 0 0 0 0 0 0 2
                                   0 0 0 1
                                   0 2
                                   0 0 0 0 0 0 0 3])) => {:_oridbag {:uuid-low 1
                                                                     :uuid-high 2
                                                                     :bag ["#2:3"]}})
       (fact "ORidBag - Tree ORid Bag"
             (t/call :link-bag-orient-type
                     (b/to-buffer [2
                                   0 0 0 0 0 0 0 1
                                   0 0 0 0 0 0 0 2
                                   0 0 0 0 0 0 0 3
                                   0 0 0 0 0 0 0 4
                                   0 0 0 5
                                   0 0 0 2
                                   0 6
                                   0 0 0 0 0 0 0 7
                                   0
                                   0 0 0 8
                                   0 9
                                   0 0 0 0 0 0 0 10
                                   1
                                   0 0 0 11])) => {:_oridtree {:uuid-low 1
                                                               :uuid-high 2
                                                               :filed-id 3
                                                               :page-index 4
                                                               :page-offset 5
                                                               :changes [{:cluster-id 6 :record-position 7
                                                                          :change-type 0 :change 8}
                                                                         {:cluster-id 9 :record-position 10
                                                                          :change-type 1 :change 11}]}})
       (fact "Decimal - BigDecimal 0"
             (t/call :decimal-orient-type
                     (b/to-buffer [0 0 0 0
                                   0 0 0 1
                                   0])) => (bigdec 0))
       (fact "Decimal - BigDecimal 1"
             (t/call :decimal-orient-type
                     (b/to-buffer [0 0 0 0
                                   0 0 0 1
                                   1])) => (bigdec 1))
       (fact "Decimal - BigDecimal 1.2"
             (t/call :decimal-orient-type
                     (b/to-buffer [0 0 0 1
                                   0 0 0 1
                                   12])) => (bigdec 1.2))
       (fact "Decimal - BigDecimal 12.34"
             (t/call :decimal-orient-type
                     (b/to-buffer [0 0 0 2
                                   0 0 0 2
                                   4 -46])) => (bigdec 12.34))
       (fact "Decimal - BigDecimal -12.34"
             (t/call :decimal-orient-type
                     (b/to-buffer [0 0 0 2
                                   0 0 0 2
                                   -5 46])) => (bigdec -12.34))
       (fact "Embedded map - Simple map"
             (t/call :embedded-map-orient-type
                     (b/to-buffer [4
                                   7 8 107 101 121 49 0 0 0 -42 7
                                   7 8 107 101 121 50 0 0 0 -35 1
                                   12 115 116 114 105 110 103 2])) => {"key1" "string", "key2" 1}))


(def record (b/to-buffer [0, 16, 76, 111, 99, 97, 116, 105, 111, 110, 8, 110, 97, 109, 101, 0, 0, 0, -102, 7, 10, 109, 101, 97, 110, 115, 0, 0, 0, -97, 14, 8, 99, 111, 115, 116, 0, 0, 0, -90, 5, 12, 115, 101, 99, 116, 111, 114, 0, 0, 0, -82, 7, 8, 108, 105, 115, 116, 0, 0, 0, -78, 11, 6, 109, 97, 112, 0, 0, 0, -65, 12, 12, 98, 105, 110, 97, 114, 121, 0, 0, 0, -34, 8, 18, 98, 111, 111, 108, 95, 116, 114, 117, 101, 0, 0, 0, -8, 0, 20, 98, 111, 111, 108, 95, 102, 97, 108, 115, 101, 0, 0, 0, -7, 0, 8, 100, 97, 116, 101, 0, 0, 0, -6, 6, 16, 100, 97, 116, 101, 116, 105, 109, 101, 0, 0, 1, 0, 6, 16, 100, 111, 99, 117, 109, 101, 110, 116, 0, 0, 1, 6, 12, 0, 8, 67, 97, 115, 97, 6, 46, 0, 44, 0, 48, 0, 64, 22, 0, 0, 0, 0, 0, 0, 6, 49, 120, 49, 6, 23, 7, 10, 77, 97, 114, 99, 111, 1, 2, 1, 6, 4, 7, 8, 107, 101, 121, 49, 0, 0, 0, -42, 7, 7, 8, 107, 101, 121, 50, 0, 0, 0, -35, 1, 12, 115, 116, 114, 105, 110, 103, 2, 50, 84, 104, 105, 115, 32, 105, 115, 32, 115, 111, 109, 101, 32, 98, 105, 110, 97, 114, 121, 32, 100, 97, 116, 97, 10, 1, 0, -128, -36, -97, -124, -87, 87, -96, -31, -114, -46, -87, 87, 6, 7, 8, 110, 97, 109, 101, 0, 0, 1, 38, 7, 7, 6, 97, 103, 101, 0, 0, 1, 44, 1, 7, 6, 109, 97, 112, 0, 0, 1, 45, 12, 10, 77, 97, 114, 99, 111, 72, 4, 7, 8, 99, 101, 108, 108, 0, 0, 1, 67, 1, 7, 6, 116, 101, 108, 0, 0, 1, 68, 1, 2, 4]))

(def expected-record
  {:_class "Location" :binary {:_obinary {:value [84 104 105 115 32 105 115 32 115 111 109 101 32 98 105 110 97 114 121 32 100 97 116 97 10]}} :date #inst "2017-07-15T22:00:00.000-00:00" :bool_true true :bool_false false, :name "Casa", :sector "1x1", :means ["#23:0" "#22:0" "#24:0"], :document {"name" "Marco", "age" 36, "map" {"cell" 1, "tel" 2}}, :list #{1 "Marco" 3}, :datetime #inst "2017-07-16T20:40:50.000-00:00", :cost 5.5, :map {"key1" "string", "key2" 1}})

(facts "Complete record deserialization"
       (t/call :record-orient-type record) => expected-record)
