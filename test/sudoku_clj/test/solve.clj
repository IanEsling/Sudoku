(ns sudoku-clj.test.solve
  (:use sudoku-clj.board))

(defn get-cell-numbers [row column cells]
  (second (first (filter #(and (= row (first (first %))) (= column (second (first %)))) cells))))

(defn count-unsolved-cells
  [cells]
  (reduce #(+ %1 (if (< 1 (count (val %2))) 1 0)) 0 cells))

(defn get-row-number [x board]
  (into {} (filter #(= x (first (key %))) board)))

(defn get-column-number [x board]
  (into {} (filter #(= x (second (key %))) board)))

(defn get-region-number [x board]
  (nth (get-regions board) (- x 1)))


