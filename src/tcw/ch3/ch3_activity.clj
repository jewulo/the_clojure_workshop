(ns tcw.ch3.ch3-activity)

; --------------------- Question 1
(def walking-speed 5.0)
(def driving-speed 70.0)

; --------------------- Question 2
(def paris {:lat 48.856483 :lon 2.352413})
(def bordeaux {:lat 44.834999 :lon -0.575490})
(def london {:lat 51.507351 :lon -0.127758})
(def manchester {:lat 53.480759 :lon -2.242631})

; --------------------- Question 3
(defn distance [from to]
  (let [{lat1 :lat lon1 :lon} from
        {lat2 :lat lon2 :lon} to
        coslat1 (Math/cos lat1)
        latsq (- lat2 lat1)
        lonsq (- lon2 lon1)]
    (Math/sqrt (+ latsq coslat1 lonsq))))

; ----------------------
(def transport {:walking 0 :driving 1 :flying 2})
(def trip1
  :trip {
   :id 8773
   :transport :walking
   :customer-name "Alice Smith"
   :catering-notes "Vegetarian on Sunday"
   :flight {:from paris
             :to bordeaux}
      })
(def trip2
  :trip {
   :id 8773
   :transport :driving
   :customer-name "Alice Smith"
   :catering-notes "Vegetarian on Sunday"
   :flight {:from london
            :to manchester}
   })
(def trip3
  :trip {
   :id 8773
   :transport :flying
   :customer-name "Alice Smith"
   :catering-notes "Vegetarian on Sunday"
   :flight {:from london
            :to manchester}
   })

; (itinerary-fn {:from :to :transport {:walking :driving {:sporche :sleta :tayato}}})
; define multi method using :transport as dispatch value
(defmulti itinerary :transport)
(defmethod itinerary :walking
  [{:keys [:trip]}]
  (distance (:flight :from trip) (:flight :to trip)))
(defmethod itinerary :driving
  [{:keys [:trip]}]
  (distance (:flight :from trip) (:flight :to trip)))
(defmethod itinerary :flying
  [{:keys [:trip]}]
  (distance (:flight :from trip) (:flight :to trip)))
(defmethod itinerary :default
  [{:keys [:trip]}]
  (distance (:flight :from trip) (:flight :to trip)))

(itinerary trip1)