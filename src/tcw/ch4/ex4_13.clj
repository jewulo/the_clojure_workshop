;; Querying Data With Filter

(ns tcw.ch4.ex4-13
  (:require
    [clojure.data.csv :as csv]
    [clojure.java.io :as io]
    [semantic-csv.core :as sc]))

; 1.
;-----------------------------------------------------------------------------------------------------------------------

; 2.
;-----------------------------------------------------------------------------------------------------------------------
(defn federer-wins [csv]
  (with-open [r (io/reader csv)]
    (->> (csv/read-csv r)
         sc/mappify
         ;; TODO: keep writing code
         (map #(select-keys % [:winner_name
                               :loser_name
                               :winner_sets_won
                               :loser_sets_won
                               :winner_games_won
                               :loser_games_won
                               :tourney_year_id
                               :tourney_slug]))
         doall)))

; 3.
;-----------------------------------------------------------------------------------------------------------------------
; use lambda predicate with filter to extract federer data
#(= "Roger Federer" (:winner_name %))

(defn federer-wins [csv]
  (with-open [r (io/reader csv)]
    (->> (csv/read-csv r)
         sc/mappify
         (filter #(= "Roger Federer" (:winner_name %)))
         (map #(select-keys % [:winner_name
                               :loser_name
                               :winner_sets_won
                               :loser_sets_won
                               :winner_games_won
                               :loser_games_won
                               :tourney_year_id
                               :tourney_slug]))
         doall)))
(federer-wins "C:\\Users\\jewul\\IdeaProjects\\the_clojure_workshop\\src\\tcw\\ch4\\match_scores_1991-2016_unindexed_csv.csv")
(take 3 (federer-wins "C:\\Users\\jewul\\IdeaProjects\\the_clojure_workshop\\src\\tcw\\ch4\\match_scores_1991-2016_unindexed_csv.csv"))