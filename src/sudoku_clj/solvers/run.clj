(ns sudoku-clj.solvers.run
  (:use sudoku-clj.solve)
  (:use sudoku-clj.board)
  (:use sudoku-clj.solvers.remove-solved-numbers-solver)
  (:use sudoku-clj.solvers.only-possible-number-solver))

(defn- count-unsolved-numbers [board]
  (reduce #(+ %1 (count %2)) 0 (numbers-of-unsolved-cells board)))

(defn solve-units [unit-solver-f get-units-f board]
  (loop [units (get-units-f board)
         newboard {}
         solved false]
    (if-let [unit (first units)]
      (if-not solved
        (let [solved-before (count (numbers-of-solved-cells unit))
              unit-after-solving (unit-solver-f unit)
              solved-after (count (numbers-of-solved-cells unit-after-solving))]
          (recur (next units) (conj newboard unit-after-solving) (< solved-before solved-after)))
        (recur (next units) (conj newboard unit) true))
      (assoc {:solved solved} :board newboard))))

(defn- solver-function [solver-f get-units-f]
  (fn [board-map]
    (if-let [solved (:solved board-map)]
      board-map
      (solve-units solver-f get-units-f (:board board-map)))))

(defn run-solvers [board & unit-solver-fs]
  (loop [fs unit-solver-fs
         board-to-solve (assoc {:solved false} :board board)]
    (if-let [f (first fs)]
      (recur (rest fs)
        ((comp
           (solver-function f get-regions)
           (solver-function f get-columns)
           (solver-function f get-rows))
          board-to-solve)
        )
      board-to-solve)))

(defn solve-board [board]
  (loop [board-to-solve board]
    (def solved-board (:board (run-solvers board-to-solve remove-solved-numbers-from-unit only-possible-number-in-unit)))
    (if-not (= (count-unsolved-numbers board-to-solve) (count-unsolved-numbers solved-board))
      (recur solved-board)
      solved-board)))
