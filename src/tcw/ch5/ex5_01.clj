(ns tcw.ch5.ex5-01)

;; ---------------- Exercise 5.01 : Finding the Day with the Maximum Temperature

;; ------------------------ 1.
(def weather-days
  [{:max 31
    :min 27
    :description :sunny
    :date "2019-09-24"}
   {:max 28
    :min 25
    :description :cloudy
    :date "2019-09-25"}
   {:max 22
    :min 18
    :description :rainy
    :date "2019-09-26"}
   {:max 23
    :min 16
    :description :stormy
    :date "2019-09-27"}
   {:max 35
    :min 19
    :description :sunny
    :date "2019-09-28"}])

;; ------------------------ 2.
; extract the maximum-values using the keyword :max
(map :max weather-days)
; find the maximum of the maximum-values
(apply max (map :max weather-days))

;; ------------------------ 3.
;; Find the the day with the maximum maximum temperature
(reduce (fn [max-day-so-far this-day]
          (if (> (:max this-day) (:max max-day-so-far))
            this-day
            max-day-so-far))
        weather-days)

;; ------------------------ 4.
;; Find the the day with the minimum maximum temperature
(reduce (fn [min-max-day-so-far this-day]
          (if (< (:max this-day) (:max min-max-day-so-far))
            this-day
            min-max-day-so-far))
        weather-days)