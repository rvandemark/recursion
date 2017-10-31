(ns recursion (:require [clojure.set :as st]))

(defn product [coll]
  (if (empty? coll)
    1
    (* (first coll) (product (rest coll)))))

(defn singleton? [coll]
  (= (count coll) 1))

(defn my-last [coll]
  (if (empty? coll)
    nil
    (if (singleton? coll)
	    (first coll)
	    (my-last (rest coll)))))

(defn max-element [a-seq]
  (if (empty? a-seq)
    nil
    (if (singleton? a-seq)
      (first a-seq)
      (max (first a-seq) (max-element (rest a-seq))))))

(defn seq-max [seq-1 seq-2]
  (let [s1 (count seq-1) s2 (count seq-2)]
    (if (> s1 s2)
      seq-1
      seq-2)))

(defn longest-sequence [a-seq]
  (cond
    (empty? a-seq) nil
    (singleton? a-seq) (first a-seq)
    :else (seq-max (first a-seq) (longest-sequence (rest a-seq)))
    ))

(defn my-filter [pred? a-seq]
  (if (empty? a-seq)
    a-seq
    (let [head (first a-seq)
          tail (rest a-seq)]
      (if (pred? head)
        (cons head (my-filter pred? tail))
        (my-filter pred? tail)))))

(defn sequence-contains? [elem a-seq]
  (cond
    (empty? a-seq) false
    (= (first a-seq) elem) true
    :else (sequence-contains? elem (rest a-seq))))

(defn my-take-while [pred? a-seq]
  (cond
    (empty? a-seq) a-seq
    (pred? (first a-seq)) (cons (first a-seq) (my-take-while pred? (rest a-seq)))
    :else []))

(defn my-drop-while [pred? a-seq]
  (cond
    (empty? a-seq) a-seq
    (pred? (first a-seq)) (my-drop-while pred? (rest a-seq))
    :else a-seq))

(defn seq= [a-seq b-seq]
  (cond
    (empty? a-seq) (empty? b-seq)
    (empty? b-seq) false
    (not (= (first a-seq) (first b-seq))) false
    :else (seq= (rest a-seq) (rest b-seq))))

(defn my-map [f seq-1 seq-2]
  (if (or (empty? seq-1) (empty? seq-2))
    []
    (let [head-1 (first seq-1)
          head-2 (first seq-2)
          tail-1 (rest seq-1)
          tail-2 (rest seq-2)]
      (cons (f head-1 head-2) (my-map f tail-1 tail-2)))))

(defn power [n k]
  (if (= k 0)
    1
    (* n (power n (- k 1)))))

(defn fib [n]
  (if (< n 2)
    n
    (+ (fib (- n 1)) (fib (- n 2)))))

(defn my-repeat [how-many-times what-to-repeat]
  (if (<= how-many-times 0)
    []
    (cons what-to-repeat (my-repeat (- how-many-times 1) what-to-repeat))))

(defn my-range [up-to]
  (if (<= up-to 0)
    []
    (cons (- up-to 1) (my-range (- up-to 1)))))

(defn tails [a-seq]
  (if (empty? a-seq)
    [[]]
    (cons a-seq (tails (rest a-seq)))))

(defn inits [a-seq]
  (map reverse (tails (reverse a-seq))))

(defn rotate-n [a-seq n]
 (if (= n 0)
  a-seq
  (rotate-n (concat (rest a-seq) (list (first a-seq))) (- n 1))))

(defn rotations [a-seq]
 (if (empty? a-seq)
  '(())
  (map (fn [n] (rotate-n a-seq n)) (range 0 (count a-seq)))))

(defn my-frequencies-helper [freqs a-seq]
  (if (empty? a-seq)
    freqs
    (let [head (first a-seq)
          tail (rest a-seq)
          prev-count (get freqs head)
          updated-frequency (if (nil? prev-count)
                              1
                              (+ prev-count 1))]
      (my-frequencies-helper (conj freqs {head updated-frequency}) tail))))

(defn my-frequencies [a-seq]
  (my-frequencies-helper {} a-seq))

(defn un-frequencies [a-map]
  (let [pairs (seq a-map)]
    (apply concat (map (fn [[elt count]] (my-repeat count elt)) pairs))))

(defn my-take [n coll]
  (if (or (= n 0) (empty? coll))
    '()
    (cons (first coll) (my-take (- n 1) (rest coll)))))

(defn my-drop [n coll]
  (if (or (= n 0) (empty? coll))
    coll
    (my-drop (- n 1) (rest coll))))

(defn halve [a-seq]
  (let [size (count a-seq)
        half (int (/ size 2))]
    [(my-take half a-seq) (my-drop half a-seq)]))

(defn seq-merge [a-seq b-seq]
  (cond
    (empty? a-seq) b-seq
    (empty? b-seq) a-seq
    :else (let [a-head (first a-seq)
                b-head (first b-seq)]
            (if (< a-head b-head)
              (cons a-head (seq-merge (rest a-seq) b-seq))
              (cons b-head (seq-merge (rest b-seq) a-seq))))))

(defn merge-sort [a-seq]
  (if (< (count a-seq) 2)
    a-seq
    (let [[left right] (halve a-seq)]
      (seq-merge (merge-sort left) (merge-sort right)))))

(defn is-monotonic? [a-seq]
  (let [consec-pairs (map vector a-seq (rest a-seq))]
    (or (every? (fn [[a b]] (< a b)) consec-pairs)
        (every? (fn [[a b]] (> a b)) consec-pairs))))

(defn longest-monotonic-prefix [a-seq]
  (if (empty? a-seq)
    '()
    (first (drop-while (complement is-monotonic?) (inits a-seq)))))

(defn split-into-monotonics [a-seq]
  (if (empty? a-seq)
    '()
    (let [mon-pfx (longest-monotonic-prefix a-seq)
          tail (drop (count mon-pfx) a-seq)]
      (cons mon-pfx (split-into-monotonics tail)))))

(defn permutations-help [rot permutations]
  (let [tail-permutations (permutations (rest rot))]
    (map (fn [tail-perm] (cons (first rot) tail-perm)) tail-permutations)))

(defn permutations [a-seq]
  (if (empty? a-seq)
    '(())
    (apply concat (map (fn [rot] (permutations-help rot permutations)) (rotations a-seq)))))

(defn powerset [a-set]
  (if (empty? a-set)
    #{#{}}
    (let [elt (first a-set)
          remaining (rest a-set)
          powerset-of-remaining (powerset remaining)]
      (st/union
        powerset-of-remaining
       (set (map (fn [s] (conj s elt)) powerset-of-remaining))))))
