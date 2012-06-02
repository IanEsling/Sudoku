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

(defn remove-solved-numbers-from-board-by-unit
  ([f-get-unit board]
    (loop [units (f-get-unit board)
           newboard {}
           solved false]
      (if-let [unit (first units)]
        (if-not solved
          (let [solved-before (count (numbers-of-solved-cells unit))
                unit-after-solving (remove-solved-numbers-from-unit unit)
                solved-after (count (numbers-of-solved-cells unit-after-solving))]
            (recur (next units) (conj newboard unit-after-solving) (< solved-before solved-after)))
          (recur (next units) (conj newboard unit) true))
        (assoc {:solved solved} :board newboard)))))

(defn solver-function
  [f]
  (fn [board-map]
    (if-let [solved (:solved board-map)]
      board-map
      (remove-solved-numbers-from-board-by-unit f (:board board-map)))))

(defn remove-solved-numbers
  [board]
  ((comp (solver-function get-regions) (solver-function get-columns) (solver-function get-rows))
    (assoc {:solved false} :board board)))