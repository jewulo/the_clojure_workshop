(ns tcw.ch4.ex4-07)

;; ---------------- Exercise 4.07 : Using comp and a Set to Filter on a Keyword

; --------------------- 1.
; https://github.com/PacktWorkshops/The-Clojure-Workshop/blob/master/Chapter04/Exercise4.07/repl.clj
(def game-users
  [{:id 9342 :username "speedy" :current-points 45 :remaining-lives 2 :experience-level 5 :status :active}
   {:id 9854 :username "stealthy" :current-points 1201 :remaining-lives 1 :experience-level 8 :status :speed-boost}
   {:id 3014 :username "sneaky" :current-points 725 :remaining-lives 7 :experience-level 3 :status :active}
   {:id 2051 :username "forgetful" :current-points 89 :remaining-lives 4 :experience-level 5 :status :imprisoned}
   {:id 1032 :username "wandering" :current-points 2043 :remaining-lives 12 :experience-level 7 :status :speed-boost}
   {:id 7213 :username "slowish" :current-points 143 :remaining-lives 0 :experience-level 1 :status :speed-boost}
   {:id 5633 :username "smarter" :current-points 99 :remaining-lives 4 :experience-level 4 :status :terminated}
   {:id 3954 :username "crafty" :current-points 21 :remaining-lives 2 :experience-level 8 :status :active}
   {:id 7213 :username "smarty" :current-points 290 :remaining-lives 5 :experience-level 12 :status :terminated}
   {:id 3002 :username "clever" :current-points 681 :remaining-lives 1 :experience-level 8 :status :active}])

(map :current-points game-users)

; --------------------- 2.
(def keep-statuses #{:active :imprisoned :speed-boost})

; --------------------- 3.
; use set keep-statues as a predicate
(filter (fn [player] (keep-statuses (:status player))) game-users)

(map :status game-users)                                    ; extract statues
(filter keep-statuses                                       ; use set keep-statues as a predicate
        (map :status game-users))
; a very naive approach would be to do this
; filter :active :imprisoned :speed-boost
(def filter-keep-statuses
  (filter (fn [player] (or (= (:status player) :active)
                           (= (:status player) :imprisoned)
                           (= (:status player) :speed-boost)))
          game-users))
; then extract :status
(map :status filter-keep-statuses)

; but with Clojure you can just do this
(filter (comp keep-statuses :status) game-users)

; --------------------- 4.
; use the threading macro ->>
(->> game-users
     (filter (comp #{:active :imprisoned :speed-boost} :status))
     (map :current-points))
