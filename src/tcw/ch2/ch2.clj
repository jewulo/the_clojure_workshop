(ns tcw.ch2.ch2)

(clojure.string/replace "Hello World" #"\w" "!")
(clojure.string/replace "Hello World" #"\w" (fn [letter] (do (println letter) "!")))
(char-array "a")
(first (char-array "a"))
(int (first (char-array "a")))
(Math/pow (int (first (char-array "a"))) 2)

; ---------------- version 1
(defn encode-letter
  [s]
  (let [code (Math/pow (int (first (char-array s))) 2)]
    (str (int code))))

;(encode-letter "a")

(defn encode
  [s]
  (clojure.string/replace s #"\w" encode-letter))

(encode "Hello World")

; ---------------- version 2
(defn encode-letter
  [s x]
  (let [code (Math/pow (+ x (int (first (char-array s)))) 2)]
    (str "#" (int code))))

(encode-letter "a" 2)

(defn encode [s]
  (let [number-of-words (count (clojure.string/split s #" "))]
    (clojure.string/replace s #"\w" (fn [s] (encode-letter s number-of-words)))))
(encode  "Super Secret")
(encode  "Super Secret Message")

(defn decode-letter
  [x y]
  (let [number (Integer/parseInt (subs x 1))
        letter (char (- (Math/sqrt number) y))]
    (str letter)))

(defn decode [s]
  (let [number-of-words (count (clojure.string/split s #" "))]
    (clojure.string/replace s #"\#\d+" (fn [s] (decode-letter s number-of-words)))))

(encode "If you want to keep a secret, you must also hide it from yourself")
;(decode *1) ; decode last REPL result. only works with a REPL
(decode (encode "If you want to keep a secret, you must also hide it from yourself"))

;;; --------------------- symbols
(def foo "bar")
foo
(defn add-2 [x] (+ x 2))
add-2

;;; --------------------- keywords
:foo
:another_keyword

;;; --------------------- maps
; literal notation
{:artist "David Bowie" :song "The Man Who Mapped the World" :year 1970}
{
 "David Bowie" {"The Man Who Mapped the World" {:year 1970, :duration "4:01"}
                "Comma Oddity" {:year 1969, :duration "5:19"}}
 "Crosby Stills Hash" {"Helplessly Mapping" {:year 1969, :duration "2:38"}
                       "Almost Cut My Hair" {:year 1970, :duration "4:29", :featuring ["Neil Young", "Rich Hickey"]}
                       }
}
; generated
(hash-map :a 1 :b 2 :c 3)
; keys must be unique.
{:name "Lucy" :age 32 :name "Jon"}                          ;Syntax error reading source at (REPL:1:35).Duplicate key: :name
; values are not unique
{:name "Lucy" :age 32 :number-of-teeth 32}

(def favourite-fruit {:name "Kiwi" :color "Green" :kcal_per_100g 61 :distinguishing_mark "Hairy"})
(get favourite-fruit :name)
(get favourite-fruit :color)
(favourite-fruit :name)
(favourite-fruit :color)
(:name favourite-fruit)
(:color favourite-fruit)
; using a nonexistent key
(:shape favourite-fruit)                                    ; => nil

; create fallback value for nonexistent key
(:shape favourite-fruit "egg-like")                         ; => egg-like
(:shape favourite-fruit)                                    ; => nil

; store new key
(assoc favourite-fruit :shape "egg-like")
favourite-fruit                                             ; remains unchanged
(def favourite-fruit-2 (assoc favourite-fruit :shape "egg-like"))
favourite-fruit-2
favourite-fruit
(:shape favourite-fruit-2)

; store a map in a map
(assoc favourite-fruit :yearly_production_in_tonnes
                       {:china 2025000 :italy 54100 :new_zealand 412000
                        :iran 311000 :chile 225000})
(def favourite-fruit-3
  (assoc favourite-fruit :yearly_production_in_tonnes
                         {:china 2025000 :italy 54100 :new_zealand 412000
                          :iran 311000 :chile 225000}))

(:yearly_production_in_tonnes favourite-fruit-3)

; updating values in a map
(assoc favourite-fruit :kcal_per_100g (- (:kcal_per_100g favourite-fruit) 1))
favourite-fruit
(update favourite-fruit :kcal_per_100g dec)
favourite-fruit

; disassociate keys
(dissoc favourite-fruit :distinguishing_mark)
favourite-fruit
(dissoc favourite-fruit :kcal_per_100g :color)
favourite-fruit

;;; --------------------- sets
; hash sets using set literal #{}
#{1 2 3 4 5}
; no duplicate entries
#{1 2 3 3 4 2 5}
#{:a :a :b :c :d :e}
; hash sets using hash-set function
(hash-set 1 2 3 4 5)
; no duplicate entries
(hash-set 1 2 3 3 4 2 5)
(hash-set :a :a :b :c :d :e)
; hash sets using set function - it takes a vector or list
(set [1 2 3 3 4 2 5])
(set [:a :a :b :c :d :e])
(set ["No" "Copy" "Cats" "Cats" "Please"])
; sorted set
(sorted-set "No" "Copy" "Cats" "Cats" "Please")

;;; --------------------- using sets
(def supported-currencies #{"Dollar" "Japanese Yen" "Euro" "Indian Rupee" "British Pound"})
; query using get
(get supported-currencies "Dollar")
(get supported-currencies "Swiss Franc")
; query using contains
(contains? supported-currencies "Dollar")
(contains? supported-currencies "Swiss Franc")
; query using container name as a function
(supported-currencies "Dollar")
(supported-currencies "Swiss Franc")
; add new items to set using conj (conjoint)
(conj supported-currencies "Monopoly Money")
(conj supported-currencies "Monopoly Money" "Gold Dragon" "Gil")
; remove new items from set using disj (disjoint)
(disj supported-currencies "Dollar")
(disj supported-currencies "Dollar" "British Pound")

;;; --------------------- vectors
[1 2 3]
(vector 10 15 2 15 0)
; construct a vector from a hash-set
(vec #{1 2 3})
; vectors are not homogeneous
[nil :keyword "String" {:answers [:yep :nope]}]
;;; --------------------- using vectors
(get [:a :b :c] 0)
(get [:a :b :c] 2)
(get [:a :b :c] 10)
(def fibonacci [0 1 1 2 3 5 8])
(get fibonacci 6)
(conj fibonacci 13 21)
; calculate the next fibonacci value
(let [size (count fibonacci)
      last-number (last fibonacci)
      second-to-last-number (fibonacci (- size 2))]
  (conj fibonacci (+ last-number second-to-last-number)))

;;; --------------------- lists
(1 2 3)                                                     ; error
'(1 2 3)
(+ 1 2 3)                                                   ; function calls are lists
'(+ 1 2 3)                                                  ; you can prevent execution with a quote '
(list :a :b :c)
(first '(:a :b :c :d))
(rest '(:a :b :c :d))
(nth '(:a :b :c :d) 2)
;;; --------------------- using lists
(def my-todo (list "Feed the cat" "Clean the bathroom" "Save the world"))
(cons "Go to work" my-todo)                                 ; cons works on lists
(conj my-todo "Go to work")                                 ; conj works on collections, a list is also a collection
(conj my-todo "Go to work" "Wash my socks")
(first my-todo)
(rest my-todo)
(nth my-todo 2)

;;; --------------------- collections and sequence abstractions
(def language {:name "Clojure" :creator "Rich Hickey" :platforms ["Java" "Javascript" ".NET"]})
(count language)
(count #{})
(empty? language)
(empty? [])
(seq language)                                              ; convert map to sequence
(nth language 1)                                            ; error a map is not a sequence
(nth (seq language) 1)
; first, rest and last work on collections directly
(first #{:a :b :c})
(rest #{:a :b :c})
(last language)
; using (into ...) to add elements to containers or sequences
; the first element is the target
(into [1 2 3 4] #{5 6 7 8})
(into #{1 2 3 4} [5 6 7 8])
(into #{} [1 2 3 3 3 4])                                    ; deduplicate a vector by putting it in a set
(into {} [[:a 1] [:b 2] [:c 3]])                            ; add tuples of [:key values] into a map
(into '() [1 2 3 4])                                        ; into uses conj
(conj '() 1)
(conj '(1) 2)
(conj '(2 1) 3)
(conj '(3 2 1) 4)
; using concat to concatenate collections
(concat '(1 2) '(3 4))
(into '(1 2) '(3 4))
(concat #{1 2 3} #{1 2 3 4})
(concat {:a 1} ["Hello"])
; sort
(def alphabet #{:a :b :c :d :e :f})
(sort alphabet)
(sort [3 7 5 1 9])
(into [] *1)                                                ; return the last result as a vector
; using conj on maps
(conj language [:created 2007])
; vectors are also maps
; they are maps of index, value pairs [:index value]
(assoc [:a :b :c :d] 2 :z)

;;; --------------------- working with nested data structures
(def gemstone-db {
   :ruby {
      :name "Ruby"
      :stock 480
      :sales [1990 3644 6376 4918 7882 6747 7495 8573 5097 1712]
      :properties {
        :dispersion 0.018
        :hardness 9.0
        :refractive-index [1.77 1.78]
        :color "Red"}}
   :diamond {
      :name "Diamond"
      :stock 10
      :sales [8295 329 5960 6118 4189 3436 9833 8870 9700 7182 7061 1579]
      :properties {
        :dispersion 0.044
        :hardness 10
        :refractive-index [2.417 2.419]
        :color "Typically Yellow, Brown or Gray to colorless"}}
   :moissanite {
      :name "Moissanite"
      :stock 45
      :sales [7761 3220]
      :properties {
         :dispersion 0.104
         :hardness 9.5
         :refractive-index [2.65 2.69]
         :color "Colorless, green, yellow"}}
  })

; these 2 are equivalent but equally cumbersome
(get (get (get gemstone-db :ruby) :properties) :hardness)         ; use get to traverse the gemstone-db
(:hardness (:properties (:ruby gemstone-db)))                     ; use keywords to traverse the gemstone-db
; better to use get-in with list of properties
(get-in gemstone-db [:ruby :properties :hardness])
; durability function takes a database and gemstone
(defn durability [db gemstone]
  (get-in db [gemstone :properties :hardness]))
(durability gemstone-db :ruby)
(durability gemstone-db :moissanite)
; updating the database
(assoc (:ruby gemstone-db) :properties {:color "Near colourless through pink through all shades of red to deep crimson"})
(into {:a 1 :b 2} {:c 3})
(update (:ruby gemstone-db) :properties into {:color "Near colourless through pink through all shades of red to deep crimson"})
(assoc-in gemstone-db [:ruby :properties :color] "Near colourless through pink through all shades of red to deep crimson")
(pprint gemstone-db)

; function to update color
(defn change-color
  [db gemstone new-color]
  (assoc-in gemstone-db [gemstone :properties :color] new-color))

(change-color gemstone-db :ruby "Some kind of Red")
gemstone-db
; modify the database using the function dec
(update-in gemstone-db [:diamond :stock] dec)
gemstone-db
; modify the number of nested list displayed
(set! *print-level* 2)
; reset the number of nested list displayed to default
(set! *print-level* nil)
; combine two operations to update database
(defn sell
  [db gemstone client-id]
  (let [clients-updated-db (update-in db [gemstone :sales] conj client-id)]
    (update-in clients-updated-db [gemstone :stock] dec)))

(sell gemstone-db :moissanite 123)
