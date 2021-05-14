(ns tcw.ch3.ch3)

;; ---------------- destructing data-structures

; ---------------- using let blocks
(defn print-coords [coords]
  (let [lat (first coords)
        lon (second coords)]
    (println (str "Latitude: " lat " - " "Longitude: " lon))))

(print-coords [1.2 3.5])

; ---------------- using sequential destructing
(defn print-coords [coords]
  (let [[lat lon] coords]
    (println (str "Latitude: " lat " - " "Longitude: " lon))))

(print-coords [1.2 3.5])

(let [[a b c] '(1 2 3)] (println a b c))

(def airport-data {:lat 48.915 :lon 2.4372 :code 'LFPB' :name "Paris Le Bourget Airport"})

(defn print-coords [airport]
  (let [lat (:lat airport)
        lon (:lon airport)
        name (:name airport)]
    (println (str name " is located at Latitude: " lat " - " "Longtitude: " lon))))

(print-coords airport-data)

(defn print-coords [airport]
  (let [{lat :lat lon :lon name :name} airport]
    (println (str name " is located at Latitude: " lat " - " "Longtitude: " lon))))

(print-coords airport-data)

;; ---------------- Exercise 3.01 : Parsing with Sequential Destructuring
(def booking [1425,"Bob Smith", "Allergic to unsalted peanuts only",
              [[48.9615,2.4372],[37.742,-25.8976]], [[37.742,-25.6976],[48.9615,2.4372]]])
; retrieve the data
(let [[id customer-name sensitive-info flight1 flight2 flight3] booking]
  (println id customer-name sensitive-info flight1 flight2 flight3))
; add more data using conj
(let [big-booking (conj booking [[37.742,-25.6976],[51.1537,0.1821]]
                        [[51.1537,0.1821],[48.9615, 2.4372]])
      [id customer-name sensitive-info flight1 flight2 flight3] big-booking]
  (println id customer-name sensitive-info flight1 flight2 flight3))
; ignore data
(let [[_ customer-name _ flight1 flight2 flight3] booking]
  (println customer-name flight1 flight2 flight3))
; create collection using let
(let [[_ customer-name _ & flights] booking]
  (println (str customer-name " booked " (count flights) " flights")))
; print nicely formatted itinerary
; version 1
(defn print-flight [flight]
  (let [[[lat1 lon1] [lat2 lon2]] flight]
    (println (str "Flying from: Lat " lat1 " Lon " lon1 " Flying to: Lat " lat2 " Lon " lon2))))
(print-flight [[48.9615, 2.4372],[37.742 -25.8976]])
; version 2
(defn print-flight [flight]
  (let [[departure arrival] flight
        [lat1 lon1] departure
        [lat2 lon2] arrival]
    (println (str "Flying from: Lat " lat1 " Lon " lon1 " Flying to: Lat " lat2 " Lon " lon2))))
(print-flight [[48.9615, 2.4372],[37.742 -25.8976]])
; version 2
(defn print-booking [booking]
  (let [[_ customer-name _ & flights] booking]
    (println (str customer-name " booked " (count flights) " flights."))
    (let [[flight1 flight2 flight3] flights]
      (when flight1 (print-flight flight1))
      (when flight2 (print-flight flight2))
      (when flight3 (print-flight flight3)))))
(print-booking booking)

;; ---------------- Exercise 3.02 : Parsing Associative Destructuring
{
 :id 8773
 :customer-name "Alice Smith"
 :catering-notes "Vegetarian on Sunday"
 :flights [
           {:from {:lat 48.9615 :lon 2.4372 :name "Paris Le Bourget Airport"},
            :to {:lat 37.742 :lon -25.6976 :name "Ponta Delgada Airport"}},

           {:from {:lat 37.742 :lon -25.6976 :name "Ponta Delgada Airport"}
            :to {:lat 48.9615 :lon 2.4372 :name "Paris Le Bourget Airport"}}
           ]
 }
; ----------- 1
(def mapjet-booking
  {
   :id 8773
   :customer-name "Alice Smith"
   :catering-notes "Vegetarian on Sunday"
   :flights [
             {:from {:lat 48.9615 :lon 2.4372 :name "Paris Le Bourget Airport"},
              :to {:lat 37.742 :lon -25.6976 :name "Ponta Delgada Airport"}},

             {:from {:lat 37.742 :lon -25.6976 :name "Ponta Delgada Airport"}
              :to {:lat 48.9615 :lon 2.4372 :name "Paris Le Bourget Airport"}}
             ]
   })
; ----------- 2
(let [{:keys [customer-name flights]} mapjet-booking]
  (println (str customer-name " booked " (count flights) " flights.")))
; ----------- 3
(defn print-mapjet-flight [flight]
  (let [{:keys [from to]} flight
        {lat1 :lat lon1 :lon} from
        {lat2 :lat lon2 :lon} to]
    (println (str "Flying from: Lat " lat1 " Lon " lon1 " Flying to: Lat " lat2 " Lon " lon2))))
(print-mapjet-flight (first (:flights mapjet-booking)))
; ----------- 4
(defn print-mapjet-flight [flight]
  (let [{{lat1 :lat lon1 :lon} :from,
         {lat2 :lat lon2 :lon} :to} flight]
    (println (str "Flying from: Lat " lat1 " Lon " lon1 " Flying to: Lat " lat2 " Lon " lon2))))
(print-mapjet-flight (first (:flights mapjet-booking)))
; ----------- 5
(defn print-mapjet-booking [booking]
  (let [{:keys [customer-name flights]} booking]
    (println (str customer-name " booked " (count flights) " flights."))
    (let [[flight1 flight2 flight3] flights]
      (when flight1 (print-mapjet-flight flight1)) flights
      (when flight2 (print-mapjet-flight flight2))
      (when flight3 (print-mapjet-flight flight3)))))
(print-mapjet-booking mapjet-booking)

;; ---------------- Advanced Call Signatures

; ---------------- Destructuring Function Parameters
(defn print-flight
  [[[lat1 lon1] [lat2 lon2]]]
  (println (str "Flying from: Lat " lat1 " Lon " lon1 " Flying to: Lat " lat2 " Lon " lon2)))
(print-flight [[48.9615, 2.4372],[37.742 -25.8976]])

(defn print-mapjet-flight
  [{ {lat1 :lat lon1 :lon} :from, {lat2 :lat lon2 :lon} :to } ]
  (println (str "Flying from: Lat " lat1 " Lon " lon1 " Flying to: Lat " lat2 " Lon " lon2)))
(print-mapjet-flight { :from {:lat 48.9615 :lon 2.4372}, :to {:lat 37.742 :lon -25.6976}})

; ---------------- Arity Overloading
(defn no-overloading []
  (println "Same old, same old..."))
(no-overloading)

(defn overloading
  ([] "No argument")
  ([a] (str "One argument: " a))
  ([a b] (str "Two arguments: a:" a " b:" b)))
(overloading)
(overloading 1)
(overloading 1 2)
(overloading 1 nil)
(overloading 1 2 3)                                         ; error: no overload with 3 parameters

; ---------------- Paranthmazes Game
(def weapon-damage {:fists 10 :staff 35 :sword 100 :cast-iron-saucepan 150})
; the function below was initially confusing. it is effectively this
; it is an overloaded function, the first overload calls the second overload
(defn strike
  ([enemy] (strike enemy :fists))
  ([enemy weapon]
   (let [damage (weapon weapon-damage)]                     ; get the weapon damage via property application :fists
     (update enemy :health - damage))))                     ; update property health using operator(-) and value damage

(strike {:name "n00b-hunter" :health 100})                      ; call first overload
(strike {:name "n00b-hunter" :health 100} :sword)               ; call second overload
(strike {:name "n00b-hunter" :health 100} :cast-iron-saucepan)  ; call second overload

; ---------------- Variadic Functions
(defn welcome
  [player & friends]
  (println (str "Welcome to the Paranthmazes " player "!"))
  (when (seq friends)
    (println (str "Sending " (count friends) " friend request(s) to the following players: "
                  (clojure.string/join ", " friends)))))
(defn welcome
  ([player]
   (println (str "Welcome to the Paranthmazes (single-player mode) " player "!")))
  ([player & friends]
   (println (str "Welcome to the Paranthmazes (multi-player mode) " player "!"))
   (println (str "Sending " (count friends) " friend request(s) to the following players: "
                 (clojure.string/join ", " friends)))))

(welcome "Jon")
(welcome "Jon" "Arya")
(welcome "Jon" "Arya" "Tyrion")
(welcome "Jon" "Arya" "Tyrion" "Petyr")

; ---------------- Multi-arity and Destructing with Parenthmazes
(def weapon-damage {:fists 10.0 :staff 35.0 :sword 100.0 :cast-iron-saucepan 150.0})
(defn strike
  ([target weapon]
   (let [points (weapon weapon-damage)]
     (if (= :gnomes (:camp target))
       (update target :health + points)
       (update target :health - points)))))
(def enemy {:name "Zulkaz", :health 250, :camp :trolls})
(strike enemy :sword)
(def ally {:name "Carla", :health 80, :camp :gnomes})
(strike ally :staff)

(defn strike
  ([target weapon]
   (let [points (weapon weapon-damage)]
     (if (= :gnomes (:camp target))
       (update target :health + points)
       (let [armor (or (:amour target) 0)                   ; if (:amour target) is nil armour is 0
             damage (* points (- 1 armor))]
         (update target :health - damage))))))
(strike enemy :cast-iron-saucepan)
(def enemy {:name "Zulkaz", :health 250, :amour 0.8 :camp :trolls})
(strike enemy :cast-iron-saucepan)

(defn strike
  ([{:keys [camp armor] :as target} weapon]
   (let [points (weapon weapon-damage)]
     (if (= :gnomes camp)
       (update target :health + points)
       (let [damage (* points (- 1 (or armor 0)))]
         (update target :health + points)
         (let [damage (* points (- 1 (or armour 0)))]
           (update target :health - damage)))))))

(defn strike
  "With one argument, strike a target with a default :fists `weapon`.
   With two arguments, strike a target with a default `weapon`.
   Strike will heal a target that belongs to the gnomes camp."
  ([target]
   (strike target :fists))
  ([{:keys [camp armour], :or {armour 0}, :as target} weapon]
   (let [points (weapon weapon-damage)]
     (if (= :gnomes camp)
       (update target :health + points)
       (let [damage (* points (- 1 armour))]
         (update target :health - damage))))))
(doc strike)
(strike enemy)
(strike enemy :cast-iron-saucepan)
(strike ally :staff)

; ---------------- Higher Order Programming
(update {:item "Tomato" :price 1.0} :price (fn [x] (/ x 2)))
(update {:item "Tomato" :price 1.0} :price / 2)             ; you can omit the lambda entirely

(update {:item "Tomato" :fruit false} :fruit not)           ; apply not to :fruit

(defn operate [f x] (f x))
(operate inc 2)
(operate clojure.string/upper-case "hello")

(defn operate [f & args] (f args))
(operate inc 2)                                             ; this will fail because & args implies a collection
(operate clojure.string/upper-case "hello")                 ; this works because "hello" is a collection

(defn operate [f & args] (apply f args))
(operate inc 2)
(operate clojure.string/upper-case "hello")
(operate str "It " "Should " "Concatenate!")

; random-fn randomly returns an arithmetic operator
(defn random-fn []
  (first (shuffle [+ - * /])))
(random-fn 2 3)                                             ; incorrect takes no parameters
((random-fn) 2 3)                                           ; (random-fn) returns an operator that takes parameters
; use fn? to test if an identifier is a function
(fn? random-fn)
(fn? (random-fn))
((random-fn) 2 3)
((random-fn) 2 3)
((random-fn) 2 3)
((random-fn) 2 3)
; returning a function from a function
(let [mysterious-fn (random-fn)]
  (mysterious-fn 2 3))
(let [mysterious-fn (random-fn)]
  (mysterious-fn 2 3))
(let [mysterious-fn (random-fn)]
  (mysterious-fn 2 3))
(let [mysterious-fn (random-fn)]
  (mysterious-fn 2 3))

; ---------------- Partial Functions
(def marketing-adder (partial + 0.99))                      ; create a partially applied function
(marketing-adder 10 5)
(def format-price (partial str "£"))                        ; create a partially applied function
(format-price "100")
(format-price 10 15)

; ---------------- Composing Functions
(defn sample1 [coll]
  (first (shuffle coll)))
(sample1 [1 2 3 4])

(def sample2 (comp first shuffle))                         ; we could redefine sample1 using function composition
(sample2 [1 2 3 4])

((comp inc *) 2 2)
((comp * inc) 2 2)                                          ; this fails because inc takes only 1 parameter

(def checkout (comp (partial str "Only ") format-price marketing-adder))
(checkout 10 5 15 6 9)

; ---------------- Anonymous Functions
; these two expressions are equivalent
(fn [s] (str "Hello" s))
#(str "Hello" %)
; so are these two
(fn [x y] (* (+ x 10) (+ y 20)))
#(* (+ %1 10) (+ %2 20))

(#(str %1 " " %2 " " %3) "First" "Second" "Third")

; ------------------- Exercise 3.04: Higher Order Functions with Parenthmazes
(def weapon-fn-map
  {:fists (fn [health] (if (< health 100) (- health 10) health))})
((weapon-fn-map :fists) 150)
((weapon-fn-map :fists) 50)

(def weapon-fn-map
  {
    :fists (fn [health] (if (< health 100) (- health 10) health))
    :staff (partial + 35)
   })
((weapon-fn-map :staff) 150)

(def weapon-fn-map
  {
   :fists (fn [health] (if (< health 100) (- health 10) health))
   :staff (partial + 35)
   :sword #(- % 100)
   })
((weapon-fn-map :sword) 150)

(def weapon-fn-map
  {
   :fists (fn [health] (if (< health 100) (- health 10) health))
   :staff (partial + 35)
   :sword #(- % 100)
   :cast-iron-saucepan #(- % 100 (rand-int 50))
   })
((weapon-fn-map :cast-iron-saucepan) 200)
((weapon-fn-map :cast-iron-saucepan) 200)

(def weapon-fn-map
  {
   :fists (fn [health] (if (< health 100) (- health 10) health))
   :staff (partial + 35)
   :sword #(- % 100)
   :cast-iron-saucepan #(- % 100 (rand-int 50))
   :sweet-potato identity
   })

(defn strike
  "With one argument, strike a target with a default :fists `weapon`.
   With two arguments, strike a target with a default `weapon`.
  Strike will heal a target that belongs to the gnomes camp."
  ([target]
   (strike target :fists))
  ([target weapon]
   (let [weapon-fn (weapon weapon-fn-map)]
     (update target :health weapon-fn))))

(def enemy {:name "Arnold" :health 250})
(strike enemy :sweet-potato)
(strike enemy :sword)
(strike enemy :cast-iron-saucepan)

; composing strikes
(strike (strike enemy :sword) :cast-iron-saucepan)
; using update to compose strikes
(update enemy
        :health
        (comp (:sword weapon-fn-map)
              (:cast-iron-saucepan weapon-fn-map)))
; using compose all strikes
(defn mighty-strike
  "Strike a `target` with all weapons!"
  [target]
  (let [weapon-fn (apply comp (vals weapon-fn-map))]
    (update target :health weapon-fn)))
(mighty-strike enemy)

; ------------------- Multi-Methods
(defmulti strike (fn [m] (get m :weapon)))                  ; define multi method using :weapon as dispatch value
(defmulti strike :weapon)                                   ; since :weapon is a keyword we can define it directly

; the second defintion returns nil because the first definition.
(ns-unmap 'user 'strike)                                    ; unmap strike in the 'user namespace
(ns-unmap 'tcw.ch3.ch3 'strike)                             ; unmap strike in the 'tcw.ch3.ch3 namespace

(defmulti strike :weapon)                                   ; re-map strike in the 'tcw.ch3.ch3 namespace

; define strike for :weapon :sword
(defmethod strike :sword
  [{{:keys [:health]} :target}]
  (- health 100))
; define strike for :weapon :cast-iron-saucepan
(defmethod strike :cast-iron-saucepan
  [{{:keys [:health]} :target}]
  (- health 100 (rand-int 50)))
; invoke methods on different weapons
(strike  {:weapon :sword :target {:health 200}})
(strike  {:weapon :cast-iron-saucepan :target {:health 200}})
; invoke method of none registered weapon spoon
(strike  {:weapon :spoon :target {:health 200}})            ; this results in a runtime exception
; to avoid runtime exceptions
; create default method for non-existent keys.
; it returns the :health value it is called with
(defmethod strike :default [{{:keys [:health]} :target}] health)
(strike  {:weapon :spoon :target {:health 200}})            ; this now results default value

(ns-unmap 'tcw.ch3.ch3 'strike)                             ; unmap strike in the 'tcw.ch3.ch3 namespace

; redefine dispatching function
(defmulti strike (fn [{{:keys [health]} :target weapon :weapon}]
                   (if (< health 50) :finisher weapon)))
(defmethod strike :finisher [_] 0)

; redefine strike for :weapon :sword
(defmethod strike :sword
  [{{:keys [:health]} :target}]
  (- health 100))
; redefine strike for :weapon :default
(defmethod strike :default [{{:keys [:health]} :target}] health)

(strike  {:weapon :sword :target {:health 200}})
(strike  {:weapon :cast-iron-saucepan :target {:health 200}})
(strike  {:weapon :spoon :target {:health 30}})

;; ---------------- Exercise 3.05 : Using Multimethods
{:name "Lea" :health 200 :position {:x 10 :y 10 :facing :north}}
(def player {:name "Lea" :health 200 :position {:x 10 :y 10 :facing :north}})

(defmulti move #(:facing (:position %)))                    ; declare a multimethod
; we can simplify move by composition
(ns-unmap 'tcw.ch3.ch3 'move)                               ;first unmap the declaration
(defmulti move (comp :facing :position))

; define overloaded methods for move on :facing
(defmethod move :north
  [entity]
  (update-in entity [:position :y] inc))

(move player)

(defmethod move :south
  [entity]
  (update-in entity [:position :y] dec))

(defmethod move :west
  [entity]
  (update-in entity [:position :x] inc))

(defmethod move :east
  [entity]
  (update-in entity [:position :x] dec))

; to use method move we do not require all the other keywords for a player
(move {:position {:x 10 :y 10 :facing :north}})
(move {:position {:x 10 :y 10 :facing :south}})
(move {:position {:x 10 :y 10 :facing :east}})
(move {:position {:x 10 :y 10 :facing :west}})

(defmethod move :default
  [entity]
  (update-in entity [:position :x] identity))
(move {:position {:x 10 :y 10 :facing :wall}})

; just do not do anything at all
(defmethod move :default
  [entity]
  entity)
(move {:position {:x 10 :y 10 :facing :wall}})