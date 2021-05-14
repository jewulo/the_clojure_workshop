(ns destructuring.destructuring)

;;https://clojure.org/guides/destructuring

;;-------------------------- Simple Destructuring --------------------------;;
(def my-line [[5 10] [10 20]])

(let [p1 (first my-line)
      p2 (second my-line)
      x1 (first p1)
      x2 (second p1)
      y1 (first p2)
      y2 (second p2)]
  (println "Line from (" x1 "," y1 ") to (" x2 ", " y2 ")"))

(let [[p1 p2] my-line
      [x1 x2] p1
      [y1 y2] p2]
  (println "Line from (" x1 "," y1 ") to (" x2 ", " y2 ")"))

;;-------------------------- Sequential Destructuring --------------------------;;
(def my-vector [1 2 3])
(def my-list '(1 2 3))
(def my-string "abc")

; It will come as no surprise that this will print out 1 2 3
(let [[x y z] my-vector]
  (println x y z))
;= 1 2 3

; We can also use a similar technique to destructure a list
(let [[x y z] my-list]
  (println x y z))
;= 1 2 3

;= For strings, the elements are destructed by character
(let [[x y z] my-string]
  (println x y z)
  (map type [x y z]))
;= a b c
;= (java.lang.Character java.lang.Character java.lang.Character)

; Bindings larger than data
(def small-list '(1 2 3))
(let [[a b c d e f g] small-list]
  (println a b c d e f g))
;= 1 2 3 nil nil nil nil

; Bindings smaller than data
(def large-list '(1 2 3 4 5 6 7 8 9 10))
(let [[a b c] large-list]
  (println a b c))
;= 1 2 3

; Accessing elements selectively
(def names ["Michael" "Amber" "Aaron" "Nick" "Earl" "Joe"])

; Very inelegant way to do bindings
(let [[item1 item2 item3 item4 item5 item6] names]
  (println item1)
  (println item2 item3 item4 item5 item6))

; Using & to combing tail elements into a sequence
(let [[item1 & remaining] names]
  (println item1)
  (apply println remaining))

; Ignoring bindings that you don't intend on using
(let [[item1 _ item3 _ item5 _] names]
  (println "Odd names:" item1 item3 item5))

; Using :as all to bind the entire vector to the symbol all (all is just a convention)
(let [[item1 :as all] names]
  (println "The first name from " all "is" item1))

; Examining the types of :as and &
(def numbers [1 2 3 4 5])
(let [[x & remaining :as all] numbers]
  (apply prn [remaining all]))
;= (2 3 4 5) [1 2 3 4 5]
; x is bound to 1
; remaining is bound to a sequence containing the remaining elements of the numbers vector apart from 1,
; all is bound to the whole vector

(def word "Clojure")
(let [[x & remaining :as all] word]
  (apply prn [x remaining all]))
;= \C (\l \o \j \u \r \e) "Clojure"
; x is bound to \C
; remaining is bound to a sequence containing the remaining elements of the numbers vector apart from \C,
; all is bound to the whole string

(def fruits ["apple" "orange" "strawberry" "peach" "pear" "lemon"])
(let [[item1 _ item3 & remaining :as all-fruits] fruits]
  (println "The first and third fruits are" item1 "and" item3)
  (println "These were taken from" all-fruits)
  (println "The fruits after them are " remaining))
;= The first and third fruits are apple and strawberry
;= These were taken from [apple orange strawberry peach pear lemon]
;= The fruits after them are (peach pear lemon)

; Destructuring can also be nested to get access to arbitrary levels of sequential structure
; (def my-line [[5 10] [10 20]]) ; defined above
(let [[[x1 y1] [x2 y2]] my-line]
  (println "Line from (" x1 "," y1 ") to (" x2 "," y2 ")"))

; When you have nested vectors, you can use :as or & at any level as well
; (def my-line [[5 10] [10 20]]) ; defined above
(let [[[a b :as group1] [c d :as group2]] my-line]
  (println a b group1)
  (println c d group2))

;;-------------------------- Associative Destructuring --------------------------;;
(def client {:name        "Super Co."
             :location    "Philadelphia"
             :description "The worldwide leader in plastic tableware."})

; Extract without destructuring
(let [name (:name client)
      location (:location client)
      description (:description client)]
  (println name location "-" description))

; Extract with associative destructuring
(let [{name        :name
       location    :location
       description :description} client]
  (println name location "-" description))

; Binding a non-existing key
(let [{category :category} client]
  (println category))

; Providing a non-existing key a default value
(let [{category :category, :or {category "Category not found"}} client]
  (println category))

; Using the :as keyword to access the entire binding during associative destructuring
(let [{name :name :as all} client]
  (println "The name from" all "is" name))

; Combining :as and :or keywords in a single destructuring
(def my-map {:a "A" :b "B" :c 3 :d 4})
(let [{a :a x :x, :or {x "Not found!"}, :as all} my-map]
  (println "I got" a "from" all)
  (println "Where is x?" x))

; Destructure a vector of keys using :keys
; This is equivalent to the first Associative Destructuring
(let [{:keys [name location description]} client]
  (println name location "-" description))

; Destructure a using :strs.
; :strs is used to destructure strings
(def string-keys {"first-name" "Joe" "last-name" "Smith"})
(let [{:strs [first-name last-name]} string-keys]
  (println first-name last-name))

; Destructure a using :syms.
; :syms is used to destructure symbols
(def symbol-keys {'first-name "Jane" 'last-name "Doe"})
(let [{:syms [first-name last-name]} symbol-keys]
  (println first-name last-name))

;;-------------------------- Combining Sequential and Associative Destructuring --------------------------;;
(def multiplayer-game-state
  {:joe  {:class  "Ranger"
          :weapon "Longbow"
          :score  100}
   :jane {:class  "Knight"
          :weapon "Greatsword"
          :score  140}
   :ryan {:class  "Wizard"
          :weapon "Mystic Staff"
          :score  150}})
(let [{{:keys [class weapon]} :joe} multiplayer-game-state]
  (println "Joe is a" class "wielding a" weapon))

;;-------------------------- Keyword Destructuring --------------------------;;
(defn configure-1 [val options]
  (let [{:keys [debug verbose] :or {debug false, verbose false}} options]
    (println "val =" val " debug =" debug " verbose =" verbose)))

(configure-1 12 {:debug true})
(configure-1 12 {:debug true :verbose true})
(configure-1 12)                                            ; error due to missing second parameter

; we can avoid the error above with variadic function and associative destructuring
(defn configure-2 [val & {:keys [debug verbose]
                          :or   {debug false, verbose false}}]
  (println "val =" val " debug =" debug " verbose =" verbose))

(configure-2 10)
(configure-2 5 :debug true)
(configure-2 12 :verbose true :debug true)

;;-------------------------- Namespaced Keyword Destructuring --------------------------;;
(def human1 {:person/name   "Franklin"
             :person/age    25
             :hobby/hobbies "running"})
(let [{:keys        [hobby/hobbies]
       :person/keys [name age]
       :or          {age 0}} human1]
  (println name "is" age "and likes" hobbies))

; clashing bindings :person/name and :hobby/name
(def human2 {:person/name "Franklin"
             :person/age  25
             :hobby/name  "running"})
(let [{:person/keys [age]
       hobby-name   :hobby/name
       person-name  :person/name} human2]
  (println person-name "is" age "and likes" hobby-name))

; you need a file person.clj for this to work
;(require '[person as p])
;(let [person {::p/name "Franklin", ::p/age 25}
;      {:keys [::p/name ::p/age]} person]
;  (println name "is" age))

; using keys for option parameters
(defn f-with-option
  [a b & {:keys [opt1]}]
  (println "Got" a b opt1))
(f-with-option 1 2)
; (f-with-option 1 2 true) ; execution error
(f-with-option 1 2 :opt1 true)

;;-------------------------- Where to Destructure --------------------------;;
(defn print-coordinates-1 [point]
  (let [x (first point)
        y (second point)
        z (last point)]
    (println "x:" x ", y:" y ",z:" z )))
(print-coordinates-1 [1 2 3])
; this can be simplified
(defn print-coordinates-2 [point]
  (let [[x y z ] point]
    (println "x:" x ", y:" y ",z:" z )))
(print-coordinates-2 [1 2 3])
; or you can apply destructuring directly unto function parameters
(defn print-coordinates-3 [[x y z ]]
  (println "x:" x ", y:" y ",z:" z ))
(print-coordinates-3 [1 2 3])

; Example 1
(def john-smith {:f-name "John"
                 :l-name "Smith"
                 :phone "555-555-5555"
                 :company "Functional Industries"
                 :title "Sith Lord of Git"})

(defn print-contact-info [{:keys [f-name l-name phone company title]}]
  (println f-name l-name "is the" title "at" company)
  (println "You can reach him at" phone))

(print-contact-info john-smith)

; Example 2
(def john-smith-2 {:f-name "John"
                   :l-name "Smith"
                   :phone "555-555-5555"
                   :address {:street "452 Lisp Ln"
                             :city "Macroville"
                             :state "Kentucky"
                             :zip "81321"}
                   :hobbies ["running" "hiking" "basketball"]
                   :company "Functional Industries"
                   :title "Sith Lord of Git"})

(defn print-contact-info-2
  [{:keys [f-name l-name phone company title]
    {:keys [street city state zip]} :address    ; extract the sub-key :address
    [fav-hobby second-hobby] :hobbies}]
  (println f-name l-name "is the" title "at" company)
  (println "You can reach him at" phone)
  (println "He lives at" street city state zip)
  (println "Maybe you can write to him about" fav-hobby "or" second-hobby))

(print-contact-info-2 john-smith-2)

;;-------------------------- Macros --------------------------;;
(def numbers [1 2 3 4 5])
(destructure '[[x & remaining :as all] numbers])