(ns tcw.ch4.ex4-12
  (:require
    [clojure.data.csv :as csv]
    [clojure.java.io :as io]
    [semantic-csv.core :as sc]))

(defn first-match [csv]
  (with-open [r (io/reader csv)]
    (->> (csv/read-csv r)
         sc/mappify
         first)))

;; test because file wasn't opening. You need the full path
;; https://stackoverflow.com/questions/7756909/in-clojure-1-3-how-to-read-and-write-a-file
(use 'clojure.java.io)
; Fails with FileNotFound Exception. You need full pathname
;(with-open [rdr (reader "match_scores_1991-2016_unindexed_csv.csv")]
  ;(doseq [line (line-seq rdr)]
;(println line)))
(with-open [rdr (reader "C:\\Users\\jewul\\IdeaProjects\\the_clojure_workshop\\src\\tcw\\ch4\\match_scores_1991-2016_unindexed_csv.csv")]
  (doseq [line (line-seq rdr)]
    (println line)))
;; --------------------------------------------------------------------------------------------

(in-ns 'tcw.ch4.ex4-12)
; Fails with FileNotFound Exception. You need full pathname
;(first-match "match_scores_1991-2016_unindexed_csv.csv")
(first-match "C:\\Users\\jewul\\IdeaProjects\\the_clojure_workshop\\src\\tcw\\ch4\\match_scores_1991-2016_unindexed_csv.csv")

;; --------------------------------------------------------------------------------------------
; using select-keys to trim the output
(defn five-matches [csv]
  (with-open [r (io/reader csv)]
    (->> (csv/read-csv r)
         sc/mappify
         (map #(select-keys % [:tourney_year_id
                               :winner_name
                               :loser_name
                               :winner_sets_won
                               :loser_sets_won]))
         (take 5)
         doall)))
(five-matches "C:\\Users\\jewul\\IdeaProjects\\the_clojure_workshop\\src\\tcw\\ch4\\match_scores_1991-2016_unindexed_csv.csv")

;; --------------------------------------------------------------------------------------------
; using sc/cast-with to convert string values to numbers
(defn five-matches-int-sets [csv]
  (with-open [r (io/reader csv)]
    (->> (csv/read-csv r)
         sc/mappify
         (map #(select-keys % [:tourney_year_id
                               :winner_name
                               :loser_name
                               :winner_sets_won
                               :loser_sets_won]))
         (sc/cast-with {:winner_sets_won sc/->int
                        :loser_sets_won sc/->int})
         (take 5)
         doall)))
(five-matches-int-sets "C:\\Users\\jewul\\IdeaProjects\\the_clojure_workshop\\src\\tcw\\ch4\\match_scores_1991-2016_unindexed_csv.csv")

