(ns sudoku-clj.solvers.run
  (:use sudoku-clj.solve)
  (:use sudoku-clj.solvers.remove-solved-numbers-solver))

(defn run-solvers
  [board]
  (loop [count-unsolved-before (reduce #(+ %1 (count %2)) 0 (numbers-of-unsolved-cells board))
         board-to-solve board]
    (def solved-board (:board (remove-solved-numbers board-to-solve)))
    (if-not (= count-unsolved-before (reduce #(+ %1 (count %2)) 0 (numbers-of-unsolved-cells solved-board)))
      (recur (reduce #(+ %1 (count %2)) 0 (numbers-of-unsolved-cells solved-board)) solved-board)
      solved-board)))
