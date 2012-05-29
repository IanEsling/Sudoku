(ns sudoku-clj.solve
  (:use clojure.set)
  (:use sudoku-clj.board))

(defn numbers-for-cells
  [f cells]
  (remove f (for [cell cells] (val cell))))

(defn numbers-of-solved-cells
  [cells]
  (numbers-for-cells #(< 1 (count %)) cells))

(defn numbers-of-unsolved-cells
  [cells]
  (numbers-for-cells #(= 1 (count %)) cells))