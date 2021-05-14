;; Using filter to Find a Tennis Rivalry

(ns tcw.ch4.ex4-15
  (:require
    [clojure.data.csv :as csv]
    [clojure.java.io :as io]
    [semantic-csv.core :as sc]))


; 1.
;-----------------------------------------------------------------------------------------------------------------------

; 2.
;-----------------------------------------------------------------------------------------------------------------------

(defn match-query [csv pred]
  (with-open [r (io/reader csv)]
    (->> (csv/read-csv r)
         sc/mappify
         (filter pred)
         (map #(select-keys % [:winner_name
                               :loser_name
                               :winner_sets_won
                               :loser_sets_won
                               :winner_games_won
                               :loser_games_won
                               :tourney_year_id
                               :tourney_slug]))
         doall)))
(def csv-file-name
  "C:\\Users\\jewul\\IdeaProjects\\the_clojure_workshop\\src\\tcw\\ch4\\match_scores_1991-2016_unindexed_csv.csv")

(match-query csv-file-name
             #(and
                (or (= (:winner_name %) "Roger Federer")
                    (= (:winner_name %) "Rafael Nadal"))
                (or (= (:loser_name %) "Roger Federer")
                    (= (:loser_name %) "Rafael Nadal"))))

;; we can simplify the above by using a set as a Predicate Function
;; see http://blog.jayfields.com/2010/08/clojure-using-sets-and-maps-as.html
;;
; This works by creating a set that uses the :winner_name and :loser_name fields,
; it then asks if the list of names "Roger Federer" "Rafael Nadal" are contained in the set
(match-query csv-file-name
             #(= (hash-set (:winner_name %) (:loser_name %))
               #{"Roger Federer" "Rafael Nadal"}))

(take 3 (match-query csv-file-name
                     #(= (hash-set (:winner_name %) (:loser_name %))
                         #{"Roger Federer" "Rafael Nadal"})))

; 3.
;-----------------------------------------------------------------------------------------------------------------------
(defn match-query [csv pred]
  (with-open [r (io/reader csv)]
    (->> (csv/read-csv r)
         sc/mappify
         (sc/cast-with {:winner_sets_won sc/->int
                        :loser_sets_won sc/int
                        :winner_games_won sc/->int
                        :loser_games_won sc/->int})
         (filter pred)
         (map #(select-keys % [:winner_name
                               :loser_name
                               :winner_sets_won
                               :loser_sets_won
                               :winner_games_won
                               :loser_games_won
                               :tourney_year_id
                               :tourney_slug]))
         doall)))

; 4.
;-----------------------------------------------------------------------------------------------------------------------
#(and (= (hash-map (:winner_name %) (:loser_name %))
         #{"Roger Federer" "Rafael Nadal"})
      (= 1 (- (:winner_sets_won %) (:loser_sets_won %))))

; 5.
;-----------------------------------------------------------------------------------------------------------------------
(match-query csv-file-name
             #(and (= (hash-map (:winner_name %) (:loser_name %))
                      #{"Roger Federer" "Rafael Nadal"})
                   (= 1 (- (:winner_sets_won %) (:loser_sets_won %)))))

(take 3 (match-query csv-file-name
                     #(and (= (hash-map (:winner_name %) (:loser_name %))
                              #{"Roger Federer" "Rafael Nadal"})
                           (= 1 (- (:winner_sets_won %) (:loser_sets_won %))))))
