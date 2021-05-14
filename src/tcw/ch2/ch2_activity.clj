(ns tcw.ch2.ch2_activity)

;; ------------------- question 1
(def memory-db (atom {}))
(defn read-db [] @memory-db)
(defn write-db [new-db] (reset! memory-db new-db))

; https://github.com/PacktWorkshops/The-Clojure-Workshop/blob/7d50777c644e1c0133d1ab4e6105d95adf80478e/Chapter02/Activity2.01/repl.clj
;; ------------------- question 2
(defn create-table
  [table-name]
  (let [db (read-db)
        new-db (assoc db table-name {:data [] :indexes {}})]
    (write-db new-db)))

(read-db)
;; ------------------- question 3
(create-table :clients)
(create-table :fruits)
(create-table :purchases)

(read-db)
;; ------------------- question 4
(defn drop-table
  [table-name]
  (let [old-db (read-db)
        new-db (dissoc old-db table-name)]
    (write-db new-db)))
;; ------------------- question 5
(drop-table :clients)
(read-db)
(create-table :clients)
;; ------------------- question 6
(comment "old version"

  (defn insert
    [table-name record id-key]
    (let [old-db (read-db)
          new-db (update-in old-db [table-name :data] conj record)
          index (- (count (get-in new-db [table-name :data])) 1)]
      (write-db
        (update-in new-db [table-name :indexes id-key] assoc (id-key record) index))))

  )

;; ------------------- question 7
(insert :clients {:id 1 :name "Bob" :age 30} :id)
(insert :clients {:id 2 :name "Alice" :age 24} :id)
(insert :fruits {:name "Lemon" :stock 10} :name)
(insert :fruits {:name "Coconut" :stock 3} :name)
(insert :purchases {:id 1 :user-id 1 :item "Coconut"} :id)
(insert :purchases {:id 1 :user-id 2 :item "Lemon"} :id)
;; ------------------- question 8
(defn select-*
  [table-name]
  (get-in (read-db) [table-name :data]))
(select-* :clients)
(select-* :fruits)
(select-* :purchases)
;; ------------------- question 9
(defn select-*-where
  [table-name field field-value]
  (let [db (read-db)
        index (get-in db [table-name :indexes field field-value])
        data (get-in db [table-name :data])]
    (get data index)))
(select-*-where :clients :id 1)                             ; you can only search on the index
(select-*-where :clients :name "Bob")
(select-*-where :fruits :name "Lemon")
(select-*-where :fruits :stock 3)
;; ------------------- question 10
(defn insert
  [table-name record id-key]
  (if-let [existing-record (select-*-where table-name id-key (id-key record))]
    (println (str "Record with " id-key ": " (id-key existing-record) " already exists. Aborting"))
    (let [db (read-db)
          new-db (update-in db [table-name :data] conj record)
          index (- (count (get-in new-db [table-name :data])) 1)]
      (write-db
        (update-in new-db [table-name :indexes id-key] assoc (id-key record) index)))))
;; ------------------- try inserting duplicate entries
(insert :clients {:id 1 :name "Bob" :age 30} :id)
(insert :fruits {:name "Lemon" :stock 10} :name)
(insert :purchases {:id 1 :user-id 1 :item "Coconut"} :id)
