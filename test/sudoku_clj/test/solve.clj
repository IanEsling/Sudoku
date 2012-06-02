(ns sudoku-clj.test.solve
  (:use sudoku-clj.board)
  (:use midje.sweet))

(defn get-cell-numbers
  [row column cells]
  (second (first (filter #(and (= row (first (first %))) (= column (second (first %)))) cells))))

(defn count-unsolved-cells
  [cells]
  (reduce #(+ %1 (if (< 1 (count (val %2))) 1 0)) 0 cells))

(defn get-row-number
  [x board]
  (into {} (filter #(= x (first (key %))) board)))

(defn get-column-number
  [x board]
  (into {} (filter #(= x (second (key %))) board)))

(defn get-region-number
  [x board]
  (nth (get-regions board) (- x 1)))

(defn update-cells-in-board
  [board cells]
  (reduce #(assoc %1 (key %2) (val %2)) board cells))

(fact "a row is solved if all cells only have one possible number"
  (count-unsolved-cells (get-row-number 1 (create-board [1 2 3 4 5 6 7 8 9]))) => 0
  (count-unsolved-cells (get-row-number 1 (create-board [1 2 3 4 5 6 7 8 0]))) => 1
  (count-unsolved-cells (get-row-number 1 (create-board [0 2 3 4 5 6 7 8 0]))) => 2
  (count-unsolved-cells (get-row-number 1 (create-board [0 2 3 4 5 6 7 8 9]))) => 1
  )

(fact "a region is solved if all cells only have one possible number"
  (count-unsolved-cells (get-region-number 1 (create-board [1 2 3 0 0 0 0 0 0
                                                            4 5 6 0 0 0 0 0 0
                                                            7 8 9 0 0 0 0 0 0]))) => 0
  (count-unsolved-cells (get-region-number 1 (create-board [1 2 3 0 0 0 0 0 0
                                                            4 0 6 0 0 0 0 0 0
                                                            7 8 9 0 0 0 0 0 0]))) => 1
  (count-unsolved-cells (get-region-number 1 (create-board [0 2 3 0 0 0 0 0 0
                                                            4 5 6 0 0 0 0 0 0
                                                            7 8 0 0 0 0 0 0 0]))) => 2
  (count-unsolved-cells (get-region-number 2 (create-board [0 0 0 1 2 3 0 0 0
                                                            0 0 0 4 5 6 0 0 0
                                                            0 0 0 7 8 9 0 0 0]))) => 0
  (count-unsolved-cells (get-region-number 3 (create-board [0 0 0 0 0 0 1 2 3
                                                            0 0 0 0 0 0 4 5 6
                                                            0 0 0 0 0 0 7 8 9]))) => 0
  )

(fact "a column is solved if all cells only have one possible number"
  (count-unsolved-cells (get-column-number 1 (create-board [1 0 0 0 0 0 0 0 0
                                                            2 0 0 0 0 0 0 0 0
                                                            3 0 0 0 0 0 0 0 0
                                                            4 0 0 0 0 0 0 0 0
                                                            5 0 0 0 0 0 0 0 0
                                                            6 0 0 0 0 0 0 0 0
                                                            7 0 0 0 0 0 0 0 0
                                                            8 0 0 0 0 0 0 0 0
                                                            9 0 0 0 0 0 0 0 0]))) => 0
  (count-unsolved-cells (get-column-number 1 (create-board [1 0 0 0 0 0 0 0 0
                                                            2 0 0 0 0 0 0 0 0
                                                            3 0 0 0 0 0 0 0 0
                                                            4 0 0 0 0 0 0 0 0
                                                            5 0 0 0 0 0 0 0 0
                                                            6 0 0 0 0 0 0 0 0
                                                            7 0 0 0 0 0 0 0 0
                                                            8 0 0 0 0 0 0 0 0
                                                            0 0 0 0 0 0 0 0 0]))) => 1
  (count-unsolved-cells (get-column-number 1 (create-board [0 0 0 0 0 0 0 0 0
                                                            2 0 0 0 0 0 0 0 0
                                                            3 0 0 0 0 0 0 0 0
                                                            4 0 0 0 0 0 0 0 0
                                                            5 0 0 0 0 0 0 0 0
                                                            6 0 0 0 0 0 0 0 0
                                                            7 0 0 0 0 0 0 0 0
                                                            8 0 0 0 0 0 0 0 0
                                                            0 0 0 0 0 0 0 0 0]))) => 2
  (count-unsolved-cells (get-column-number 1 (create-board [0 0 0 0 0 0 0 0 0
                                                            2 0 0 0 0 0 0 0 0
                                                            3 0 0 0 0 0 0 0 0
                                                            4 0 0 0 0 0 0 0 0
                                                            5 0 0 0 0 0 0 0 0
                                                            6 0 0 0 0 0 0 0 0
                                                            7 0 0 0 0 0 0 0 0
                                                            8 0 0 0 0 0 0 0 0
                                                            9 0 0 0 0 0 0 0 0]))) => 1
  )

