(ns tcw.ch4.ch4-activity-4-02
  (:require
    [clojure.data.csv :as csv]
    [clojure.java.io :as io]
    [semantic-csv.core :as sc]))

;; https://github.com/PacktWorkshops/The-Clojure-Workshop/blob/master/Chapter04/Activity4.02/Activity4.02.clj

;; on REPL
;(require '[clojure.data.csv :as csv])
;(require '[clojure.java.io :as io])
;(require '[semantic-csv.core :as sc])

;; ---------------- Activity 4.02 : Arbitrary Tennis Rivalries

;; Note to self, I wouldn't have dreamt of the solution.
;; My thinking is still too procedural rather than functional and declarative!

(defn rivalry-data [csv player-1 player-2]
  (with-open [r (io/reader csv)]
    (let [rivalry-seq (->> (csv/read-csv r)
                           sc/mappify
                           (sc/cast-with {:winner_sets_won sc/->int
                                          :loser_sets_won sc/->int
                                          :winner_games_won sc/->int
                                          :loser_games_won sc/->int})
                           (filter #(= (hash-set (:winner_name %) (:loser_name %))
                                       #{player-1 player-2}))
                           (map #(select-keys % [:winner_name
                                                 :loser_name
                                                 :winner_sets_won
                                                 :loser_sets_won
                                                 :winner_games_won
                                                 :loser_games_won
                                                 :tourney_year_id
                                                 :tourney_slug])))
          player-1-victories (filter #(= (:winner_name %) player-1) rivalry-seq)
          player-2-victories (filter #(= (:winner_name %) player-2) rivalry-seq)]
      {:first-victory-player-1 (first player-1-victories)
       :first-victory-player-2 (first player-2-victories)
       :total-matches (count rivalry-seq)
       :total-victories-player-1 (count player-1-victories)
       :total-victories-player-2 (count player-2-victories)
       :most-competitive-matches (->> rivalry-seq
                                      (filter #(= 1 (- (:winner_sets_won %) (:loser_sets_won %)))))})))

(def csv-file-name
  "C:\\Users\\jewul\\IdeaProjects\\the_clojure_workshop\\src\\tcw\\ch4\\match_scores_1991-2016_unindexed_csv.csv")
(rivalry-data csv-file-name "Boris Becker" "Jimmy Connors")
