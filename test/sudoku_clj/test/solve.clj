(ns sudoku-clj.test.solve
  (:use midje.sweet)
  (:use sudoku-clj.solve)
  (:use sudoku-clj.board)
  )

(defn random-board []
  (create-board (repeatedly 81 (partial rand-int 10))))

(defn get-cell-numbers [row column cells]
  (second (first (filter #(and (= row (first (first %))) (= column (second (first %)))) cells))))

(fact "a board can be converted into 9 rows"
  (count (get-rows (random-board))) => 9)

(fact "a board can be converted into 9 columns"
  (count (get-columns (random-board))) => 9)

(fact "a board can be converted into 9 regions"
  (count (get-regions (random-board))) => 9)

(fact "each row contains 9 cells"
  (doseq [row (get-rows (random-board))]
    (count row) => 9))

(fact "each column contains 9 cells"
  (doseq [column (get-columns (random-board))]
    (count column) => 9))

(fact "each region contains 9 cells"
  (doseq [column (get-regions (random-board))]
    (count column) => 9))