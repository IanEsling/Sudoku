(ns sudoku-clj.solvers.only-possible-number-solver
  (:use sudoku-clj.board)
  (:use sudoku-clj.solve)
  (:use clojure.set)
  )

(defn map-of-unsolved-numbers
  [unit]
  (reduce #(into %1 {%2 0}) {} (apply union (numbers-of-unsolved-cells unit))))

(defn frequencies-of-unsolved-numbers
  [unit]
  (reduce #(reduce (fn [t x] (assoc t x (+ 1 (get t x)))) %1 %2)
    (map-of-unsolved-numbers unit)
    (numbers-of-unsolved-cells unit)))

(defn only-possible-number-in-unit
  [unit]
  (def freq (into {} (filter #(= 1 (val %)) (frequencies-of-unsolved-numbers unit))))
  (reduce (fn
            [newunit cell]
            (if (= 1 (count (intersection (val cell) (set (keys freq)))))
              (into newunit {(first cell) (set (intersection (val cell) (set (keys freq))))})
              (into newunit {(first cell) (second cell)}))
            ) {} unit))