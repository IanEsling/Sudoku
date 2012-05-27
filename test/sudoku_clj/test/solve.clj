(ns sudoku-clj.test.solve
  (:use sudoku-clj.solve)
  (:use sudoku-clj.board)
  )

(defn random-board []
  (create-board (repeatedly 81 (partial rand-int 10)))
  )

(defn get-cell [row column cells]
  (into {} (filter #(and (= row (first (first %))) (= column (second (first %)))) cells))
  )

(defn solved? [cells]
  (not (< 0 (count-unsolved-cells cells))))

