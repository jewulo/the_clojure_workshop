(ns tcw.ch5.ch5)

;; ---------------- Introduction
(reduce (fn [sum-so-far item] (+ sum-so-far item)) [8 4000 10 300])
(reduce + [8 4000 10 300])
(apply + [8 4000 10 300])

;; ---------------- Initialising Reduce
(reduce + [1 2 3 5 7])                                      ; => 18
; this reduces to
(+ 1 2)
(+ 3 3)
(+ 6 5)
(+ 11 7)                                                    ; => 18

(reduce (fn [{:keys [minimum maximum]} new-number]
          {:minimum (if (and minimum (> new-number minimum))
                      minimum
                      new-number)
           :maximum (if (and maximum (< new-number maximum))
                      maximum
                     new-number)})
        {}      ; <----- The new argument to reduce - the initial value
        [5 23 5004 845 22])

;; ---------------- Partitioning with Reduce
(partition 3 [1 2 3 4 5 6 7 8 9 10])
(partition-all 3 [1 2 3 4 5 6 7 8 9 10])
(partition-by #(> % 10) [5 33 18 0 23 2 9 4 3 99])

; Imagine creating a set of partitions from a given sequence were
; the sum of each partition was less than 20
; For Example:
{:current [5 10]
 :segments [[3 7 8]
            [17]
            [4 1 1 5 3 2]]}

(reduce (fn [{:keys [segments current] :as accum} n]
          (let [current-with-n (conj current n)
                total-with-n (apply + current-with-n)]
            (if (> total-with-n 20)
              (assoc accum
                     :segments (conj segments current)
                     :current [n])
              (assoc accum :current current-with-n))))
        {:segments [] :current []}
        [4 19 4 9 5 12 5 3 4 1 1 9 5 18])

(defn segment-by-sum [limit ns]
  (let [result (reduce (fn [{:keys [segments current] :as accum} n]
                         (let [current-with-n (conj current n)
                               total-with-n (apply + current-with-n)]
                           (if (> total-with-n limit)
                             (assoc accum
                                    :segments (conj segments current)
                                    :current [n])
                             (assoc accum :current current-with-n))))
                       {:segments [] :current []}
                       ns)]
    (conj (:segments result) (:current result))))
(segment-by-sum 20 [4 19 4 9 5 12 5 3 4 1 1 9 5 18])

;; ---------------- Looking Back with Reduce
; defined symbol keys are used to reference and look back at the
; recursively generated sequence
(def numbers [4 9 2 3 7 9 5 2 6 1 4 6 2 3 3 6 1])
; I really struggled to understand this function
(defn parity-totals [ns]
  (:ret
    (reduce (fn [{:keys [current] :as acc} n]
              (if (and (seq current)
                       (or (and (odd? (last current)) (odd? n))
                           (and (even? (last current)) (even? n))))
                (-> acc
                    (update :ret conj [n (apply + current)])
                    (update :current conj n))
                (-> acc
                    (update :ret conj [n 0])
                    (assoc :current [n]))))
            {:current [] :ret []}
            ns)))
(parity-totals numbers)
;--------------- writing parity-totals without the enclosing :ret
(defn parity-totals-2 [ns]
  (reduce (fn [{:keys [current] :as acc} n]
            (if (and (seq current)
                     (or (and (odd? (last current)) (odd? n))
                         (and (even? (last current)) (even? n))))
              (-> acc
                  (update :ret conj [n (apply + current)])
                  (update :current conj n))
              (-> acc
                  (update :ret conj [n 0])
                  (assoc :current [n]))))
          {:current [] :ret []}
          ns))
(parity-totals-2 numbers)
; parity-totals-2 returns the results for both :current and :ret
; parity-totals-2 displays the symbol :ret and :current
; parity-totals returns the results for only :ret
; parity-totals does not display the symbol :ret or :current
; I get it.
; By enclosing the code within :ret
; we are saying that we are not concerned
; with the results of the key :current
; see parity-totals-3 below. results are stored in :current
; but it is not displayed in the results
;------------------------------------------------------
(defn parity-totals-3 [ns]
  (:current
    (reduce (fn [{:keys [current] :as acc} n]
              (if (and (seq current)
                       (or (and (odd? (last current)) (odd? n))
                           (and (even? (last current)) (even? n))))
                (-> acc
                    (update :ret conj [n (apply + current)])
                    (update :current conj n))
                (-> acc
                    (update :ret conj [n 0])
                    (assoc :current [n]))))
            {:current [] :ret []}
            ns)))
(parity-totals-3 numbers)
; these might give you an insight to the above
; these are equivalent
; ---- 1.
(update {:cur [] :ret []} :ret conj [2 0])
(-> {:cur [] :ret []}
    (update :ret conj [2 0]))
; ---- 2.
(update {:cur [] :ret []} :cur conj 4)
(-> {:cur [] :ret []}
    (update :cur conj 4))
; ---- 3
(assoc {:cur [] :ret []} :cur [4])
(-> {:cur [] :ret []}
    (assoc :cur [4]))
; ---- 4
(-> {:cur [] :ret []}
    (update :ret conj 4)
    (assoc  :cur [3]))
(assoc
  (update {:cur [] :ret []} :ret conj 4)
  :cur
  [3])
