(ns sudoku-clj.solvers.remove-solved-numbers-solver
  (:use clojure.set)
  (:use sudoku-clj.solve)
  (:use sudoku-clj.board)
  )

(defn remove-solved-numbers-from-unit
  [unit]
  (def solved-numbers (apply union (numbers-of-solved-cells unit)))
  (reduce (fn [newunit cell]
            (if-not (= 1 (count (second cell)))
              (assoc newunit (first cell) (difference (second cell) solved-numbers))
              newunit
              ))
    unit unit))


