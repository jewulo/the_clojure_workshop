(ns tcw.ch3.ch3-activity-github)

;; ------------------- Results from GitHub
;; https://github.com/PacktWorkshops/The-Clojure-Workshop/blob/master/Chapter03/Activity3.01/repl.clj

;; km/h
(def walking-speed 4)
(def driving-speed 70)
(def vehicle-cost-fns
  {
   :sporche (partial * 0.12 1.3)
   :tayato (partial * 0.07 1.3)
   :sleta (partial * 0.2 0.1)
   })

(defn distance
  "Returns the rough estimate of the distance between two coordinate points,
  in kilometers. Works best with smaller distance"
  [{lat1 :lat lon1 :lon} {lat2 :lat lon2 :lon}]
  (let [deglen 110.25
        x (- lat2 lat1)
        y (* (Math/cos lat2) (- lon2 lon1))]
    (* deglen (Math/sqrt (+ (* y y) (* x x))))))

(defmulti itinerary
          "Calculate the distance of travel between two locations, and the
          cost and duration based on the value of :transport"
          :transport)

(defmethod itinerary :walking
  [{:keys [:from :to]}]
  (let [walking-distance (distance from to)
        duration (/ (distance from to) walking-speed)]
    {:cost 0 :distance walking-distance :duration duration}))

(defmethod itinerary :driving
  [{:keys [:from :to :vehicle]}]
  (let [driving-distance (distance from to)
        vcost-fn (vehicle vehicle-cost-fns)
        cost (vcost-fn driving-distance)
        duration (/ driving-distance driving-speed)]
    {:cost cost :distance driving-distance :duration duration}))

(def paris {:lat 48.856483 :lon 2.352413})
(def bordeaux {:lat 44.834999 :lon -0.575490})
(def london {:lat 51.507351 :lon -0.127758})
(def manchester {:lat 53.480759 :lon -2.242631})

(itinerary {:from paris :to bordeaux :transport :walking})
(itinerary {:from paris :to bordeaux :transport :driving :vehicle :tayato})
(itinerary {:from london :to manchester :transport :walking})
(itinerary {:from manchester :to london :transport :driving :vehicle :sleta})
