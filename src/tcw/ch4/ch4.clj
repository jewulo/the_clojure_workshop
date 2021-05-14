(ns tcw.ch4.ch4)

;; ---------------- map
(map inc [1 2 3])

;; ---------------- Exercise 4.01 : Working with map
; ----------- 1
(map (fn [i] (* i 10)) [1 2 3 4 5])
; ----------- 2
(map count ["Let's" "measure" "word" "length" "now"])
; ----------- 3
(map (fn [w] (str w ": " (count w))) ["Let's" "measure" "word" "length" "now"])

;; ---------------- filter
(filter keyword? ["a" :b "c" :d "e" :f "g"])

;; ---------------- Exercise 4.02 : Getting Started with filter
; ----------- 1
(odd? 5)
; ----------- 2
(odd? 6)
; ----------- 3
(filter odd? [1 2 3 4 5])
; ----------- 4
(remove odd? [1 2 3 4 5])                                   ; remove is the alter-ego of filter: it does the inverse
; ----------- 5
(constantly true)
(filter (constantly true) [1 2 3 4 5])
(filter (constantly false) [1 2 3 4 5])

;; ---------------- Other members of the filter family
(take 3 [1 2 3 4 5])
(drop 3 [1 2 3 4 5])
(take-while #(> 10 %) [2 9 4 12 3 99 1])
(drop-while #(> 10 %) [2 9 4 12 3 99 1])

;; ---------------- Exercise 4.03 : Partitioning a Sequence with take-while and drop-while
; ----------- 1
(def students [{:name "Eliza" :year 1994}
               {:name "Salma" :year 1995}
               {:name "Jodie" :year 1997}
               {:name "Kaitlyn" :year 2000}
               {:name "Alice" :year 2001}
               {:name "Pippa" :year 2002}
               {:name "Fleur" :year 2002}])
; ----------- 2
#(< (:year %) 2000)
; ----------- 3
(take-while #(< (:year %) 2000) students)
; ----------- 4
(drop-while #(< (:year %) 2000) students)

;; ---------------- Using map and filter together
(map (fn [n] (* 10 n))
     (filter odd? [1 2 3 4 5]))

;; ---------------- Threading Macros
(def filtered (filter odd? [1 2 3 4 5]))
(map (fn [n] (* 10 n)) filtered)

(->> [1 2 3 4 5]
     (filter odd?)
     (map (fn [n] (* 10 n))))

;; ---------------- Using Lazy Sequences
(def v1 [1 2 3 4 5 6])                                      ; is a literal vector
(def v2 (range 1 6))                                        ; is a lazy sequence

(def our-seq (range 100))                                   ; is a lazy sequence
(first our-seq)                                             ; causes range to return only the first element
(second our-seq)                                            ; causes range to invoke inc to get the second element
(last our-seq)                                              ; causes range to invoke inc until the last element
                                                            ; note: lazy sequences behave differently in the REPL
                                                            ;
                                                            ; map, filter and remove are also lazy operations

;; ---------------- Exercise 4.04 : Watching Lazy Evaluation
; ----------- 1
(defn our-range [limit]
  (take-while #(< % limit) (iterate inc 0)))
; ----------- 2
(def or5 (our-range 5))
; ----------- 3
(map #(* 10 %) (our-range 5))
; ----------- 4
(map (fn [i] (print ".") (* i 10)) (our-range 5))
; ----------- 5
(def by-ten (map (fn [i] (print ".") (* i 10)) (our-range 5)))
; ----------- 6
by-ten
(range)                                                     ; calling range without a parameter can crash the platform
; putting range in a sequence operator allows for lazy evaluation
; (take 5) determines the number of operations
(->> (range)
     (map #(* 10 %))
     (take 5))
; but the following could cause a crash.
; !!!!!!!!!!!!!!!!!!!!!! DO NOT DO THIS !!!!!!!!!!!!!!!!!!!!!!!!!
(->> (range)
     (map #(* 10 %))
     (last))

;; ---------------- Exercise 4.05 : Creating our own lazy sequence
; ----------- 1
(def our-randoms (repeatedly (partial rand-int 100)))
; ----------- 2 use take to limit the values from our-randoms
(take 20 our-randoms)
(def our-randoms2 (repeatedly (rand-int 100)))
(take 20 our-randoms2)
; ----------- 1
(defn some-random-integers [size]
  (take size (repeatedly (fn [] (rand-int 100)))))
(some-random-integers 12)

;; ---------------- Anonymous  Functions
; in Clojure anonymous function can be specified in two ways
; 1. Using keyword fn
(fn [n] (* 10 n))
(fn [x y] (* x y))
(fn [x y z] (* x y z))
; 2. Using #() expressions
#(* 10 %)
#(* %1 %2)
#(* %1 %2 %3)
; 3. Using partial
(map (partial * 10) [1 2 3 4 5])
(def apart (partial * 10))
(apart 5)
(map apart [1 2 3 4 5])

; Note: (partial * 10) <=> (fn [x] (* 10 x))
;; ---------------- Keywords as Functions
; In Clojure keywords can be used as functions whose argument is a map
(:my-field {:my-field 42})
(:my-field {:my-field 42 :another-field 43})
(:my-field {:another-field 43})

;; ---------------- Exercise 4.06 : Extracting Data from a List of Maps
;; see file ex4-06.clj

;; ---------------- Sets as Predicates
(def alpha-set (set [:a :b :c]))
(alpha-set :a)
(alpha-set :z)

; hash set takes zero or more individual items instead of a sequence
(hash-set :a :b :c :a)
(def animal-names ["turtle" "horse" "cat" "frog" "hawk" "worm"])
(remove (fn [animal-name]
          (or (= animal-name "horse")
              (= animal-name "cat")))
        animal-names)
; the expression above could be reduced by using a set as a predicate
(remove #{"horse" "cat"} animal-names)
; you can actually invoke the set literal like a function
(#{"horse" "cat"} "cat")
(#{"horse" "cat"} "horse")
(#{"horse" "cat"} "dog")
(#{"horse" "cat"} "rat")

;; ---------------- Filtering on a Keyword with comp and Set
(require '[clojure.string :as string])
(defn normalize [s] (string/trim (string/lower-case s)))
(normalize "          Some Information             ")
; or we could use comp to compose
(def normalizer (comp string/trim string/lower-case))
(normalizer "          Some Information             ")

(def remove-words #{"and" "an" "a" "the" "of" "is"})
(remove (comp remove-words string/lower-case string/trim)
        ["Feburuary" " THE " "4th"])

;; ---------------- Exercise 4.07 : Using comp and a Set to Filter on a Keyword
;; see ex4-07.clj

;; ---------------- Returning a List Longer than the Input with mapcat
(def alpha-lc ["a" "b" "c" "d" "e" "f" "g" "h" "i" "j"])
; why mapcat? because map will returning a list tuples
(map (fn [letter]
       [letter (clojure.string/upper-case letter)])
     alpha-lc)
; mapcat will unwrap all the results
(mapcat (fn [letter]
          [letter (clojure.string/upper-case letter)])
        alpha-lc)

;; ---------------- Mapping with multiple inputs
(map (fn [a b] (+ a b)) [5 8 3 1 2] [5 2 7 9 8])

(defn our-zipmap [xs ys]
  (->> (map (fn [x y] [x y]) xs ys)
       (into {})))
(our-zipmap [:a :b :c] [1 2 3])
; there already exists a clojure zipmap
(zipmap [:a :b :c] [1 2 3])

(def meals ["breakfast" "lunch" "dinner" "midnight snack"])
(map (fn [idx meal] (str (inc idx) ". " meal)) (range) meals)
; you can do this without range with map-indexed
(map-indexed (fn [idx meal] (str (inc idx) ". " meal)) meals)
; looking ahead with map. the mapping will stop on the shortest range
(def v1 [1 2 3 4 5 6])
(map (fn [x next-x ] [x next-x])
     v1
     (rest v1))

;; Partitioning a sequence
(def v3 [1 2 3 4 5 6 7 8 9 10])
(partition 2 v3)

;; ---------------- Exercise 4.08 : Identifying Weather Trends
; ----------- 1
(def temperature-by-day
  [18 23 24 27 24 22 21 21 20 32 33 30 29 35 28 25 24 28 29 30])

; ----------- 2
(map (fn [today yesterday]
       (cond (> today yesterday) :warmer
             (< today yesterday) :colder
             (= today yesterday) :unchanged))
     (rest temperature-by-day)
     temperature-by-day)

;; ---------------- Consuming Extracted Data with apply
(apply max [3 9 6]) ; apply pulls/extracts the argument out of the sequence
; is equivalent to
(max 3 9 6)

; apply can be used in combination with other Higher-Order functions
; this fails because b is not numeric
(let [a 5
      b nil
      c 18]
  (+ a b c))
; we could combining apply and filter to get the correct data
(let [a 5
      b nil
      c 18]
(apply + (filter integer? [a b c])))
; using apply with functions of 1 or more arity
; + as 0 or more arity, so this is fine
(apply + [])
; min as 1 or more arity
(apply min [])                                                ; will fail
(apply min 0 [])                                              ; will work

;; ---------------- Exercise 4.09 : Finding the Average Weather Temperature
; ----------- 1
(def temperature-by-day
  [18 23 24 27 24 22 21 21 20 32 33 30 29 35 28 25 24 28 29 30])
; ----------- 2
(let [total (apply + temperature-by-day)
      c (count temperature-by-day)]
  (/ total c))

;; ---------------- Activity 4.01 : Using map and filter to Report Summary Information
;; see ch4_activity_4_01.clj

;; ---------------- Exercise 4.10 : Identifying Weather Trends
;; had to add [org.clojure/data.csv "1.0.0"] to dependencies in project.clj
;;; ---- This Works In The REPL
(require '[clojure.data.csv :as csv])
(require '[clojure.java.io :as io])

(with-open [r (io/reader "C:\\Users\\jewul\\IdeaProjects\\the_clojure_workshop\\src\\tcw\\ch4\\match_scores_1991-2016_unindexed_csv.csv")]
  (first (csv/read-csv r)))

(with-open [r (io/reader "C:\\Users\\jewul\\IdeaProjects\\the_clojure_workshop\\src\\tcw\\ch4\\match_scores_1991-2016_unindexed_csv.csv")]
  (count (csv/read-csv r)))

;; ---------------- Exercise 4.11 : Avoiding Lazy Evaluation
;;; ---- This Works In The REPL
(require '[clojure.data.csv :as csv])
(require '[clojure.java.io :as io])

(with-open [r (io/reader "C:\\Users\\jewul\\IdeaProjects\\the_clojure_workshop\\src\\tcw\\ch4\\match_scores_1991-2016_unindexed_csv.csv")]
  (->> (csv/read-csv r)
       (map #(nth % 7))
       (take 6)))

; doall  force the evaluation of a lazy sequence
(with-open [r (io/reader "C:\\Users\\jewul\\IdeaProjects\\the_clojure_workshop\\src\\tcw\\ch4\\match_scores_1991-2016_unindexed_csv.csv")]
  (->> (csv/read-csv r)
       (map #(nth % 7))
       (take 6)
       doall))


