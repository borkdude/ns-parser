(ns borkdude.ns-parser-test
  (:require [clojure.test :as t :refer [deftest is testing]]
            [rewrite-clj.parser :as p]
            [rewrite-clj.node :as n]
            [borkdude.ns-parser :as nsp]
            [borkdude.deflet :refer [deflet]]))

(deftest ns-state-rewrite-clj-test
  (deflet
    (def ns-form (p/parse-string "(ns foo (:require [bar :as b]))"))
    (def parsed-ns-form (nsp/parse-ns-form (n/sexpr ns-form)))
    (def rewrite-clj-ns-resolver (assoc (:aliases parsed-ns-form) :current (:name parsed-ns-form)))
    (testing "resolved in current ns"
      (is (= :foo/foo (n/sexpr (p/parse-string "::foo") {:auto-resolve rewrite-clj-ns-resolver}))))
    (testing "resolved with alias in current ns"
      (is (= :bar/foo (n/sexpr (p/parse-string "::b/foo") {:auto-resolve rewrite-clj-ns-resolver}))))))
