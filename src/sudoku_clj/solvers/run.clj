(ns sudoku-clj.solvers.run
  (:use sudoku-clj.solve)
  (:use sudoku-clj.solvers.remove-solved-numbers-solver))

(defn count-unsolved-numbers
  [board]
  (reduce #(+ %1 (count %2)) 0 (numbers-of-unsolved-cells board)))

(defn run-solvers
  [board]
  (loop [board-to-solve board]
    (def solved-board (:board (remove-solved-numbers board-to-solve)))
    (if-not (= (count-unsolved-numbers board-to-solve) (count-unsolved-numbers solved-board))
      (recur solved-board)
      solved-board)))
