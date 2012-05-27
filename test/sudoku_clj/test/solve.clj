(ns sudoku-clj.test.solve
  (:use sudoku-clj.solve)
  )

(defn get-cell [row column cells]
  (into {} (filter #(and (= row (first (first %))) (= column (second (first %)))) cells))
  )

(defn solved? [cells]
  (not (< 0 (count-unsolved-cells cells))))

