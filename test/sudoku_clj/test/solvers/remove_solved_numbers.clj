(ns sudoku-clj.test.solvers.remove-solved-numbers
  (:use midje.sweet)
  (:use sudoku-clj.solve)
  (:use sudoku-clj.board)
  (:use sudoku-clj.test.solve)
  (:use sudoku-clj.solvers.remove-solved-numbers-solver)
  (:use [sudoku-clj.solvers.run :exclude (solved-board)])
  )

(fact "an unsolved cell will have the numbers in solved cells in the same row removed"
  (def board (create-board [1 2 3 4 5 6 7 0 0]))
  (count (filter #(= 2 (count (second %)))
           (into {} (filter #(= 1 (first (key %)))
                      (into {} (remove-solved-numbers-from-unit (get-row-number 1 board))))))) => 2
  ;should both be a set of 8 and 9
  (get-cell-numbers 1 8 (remove-solved-numbers-from-unit (get-row-number 1 board))) => #{8 9}
  (get-cell-numbers 1 9 (remove-solved-numbers-from-unit (get-row-number 1 board))) => #{8 9}
  )

(fact "an unsolved region will have the numbers in solved cells in the same region removed"
  (def board (create-board [1 2 3 0 0 0 0 0 0
                            4 5 6 0 0 0 0 0 0
                            7 0 0 0 0 0 0 0 0]))
  (count (filter #(= 2 (count (second %)))
           (remove-solved-numbers-from-unit (get-region-number 1 board)))) => 2
  ;should both be a set of 8 and 9
  (get-cell-numbers 3 2 (remove-solved-numbers-from-unit (get-region-number 1 board))) => #{8 9}
  (get-cell-numbers 3 3 (remove-solved-numbers-from-unit (get-region-number 1 board))) => #{8 9}
  )

(fact "an unsolved cell will have the numbers in solved cells in the same column removed"
  (def board (create-board [1 0 0 0 0 0 0 0 0
                            2 0 0 0 0 0 0 0 0
                            3 0 0 0 0 0 0 0 0
                            4 0 0 0 0 0 0 0 0
                            5 0 0 0 0 0 0 0 0
                            6 0 0 0 0 0 0 0 0
                            7 0 0 0 0 0 0 0 0
                            0 0 0 0 0 0 0 0 0
                            0 0 0 0 0 0 0 0 0]))
  (count (filter #(= 2 (count (second %)))
           (into {} (filter #(= 1 (second (key %)))
                      (into {} (remove-solved-numbers-from-unit (get-column-number 1 board)))))
           )) => 2
  ;should both be a set of 8 and 9
  (get-cell-numbers 8 1 (remove-solved-numbers-from-unit (get-column-number 1 board))) => #{8 9}
  (get-cell-numbers 9 1 (remove-solved-numbers-from-unit (get-column-number 1 board))) => #{8 9}
  )

(fact "only the first solvable cell will be solved for regions"
  (def board (create-board [1 4 7 2 0 8 3 6 9
                            2 5 8 3 6 9 1 4 7
                            3 6 0 1 4 7 2 5 8
                            4 7 1 0 0 0 0 0 0
                            5 8 2 0 0 0 0 0 0
                            6 9 3 0 0 0 0 0 0
                            7 1 4 0 0 0 0 0 0
                            0 2 5 0 0 0 0 0 0
                            9 3 6 0 0 0 0 0 0]))
  (def solved-board (into {} (:board (solve-units remove-solved-numbers-from-unit get-regions board))))
  (count solved-board) => 81
  (count (get-region-number 1 solved-board)) => 9
  (count (get-region-number 2 solved-board)) => 9
  (count (get-region-number 3 solved-board)) => 9
  (count-unsolved-cells (get-region-number 1 solved-board)) => 0
  (count-unsolved-cells (get-region-number 2 solved-board)) => 1
  (count-unsolved-cells (get-region-number 3 solved-board)) => 0
  (count-unsolved-cells (get-region-number 5 solved-board)) => 9
  (count-unsolved-cells (get-region-number 7 solved-board)) => 1
  (get-cell-numbers 3 3 solved-board) => #{9}
  (get-cell-numbers 8 1 solved-board) => #{1 2 3 4 5 6 7 8 9}
  )

(fact "only the first solvable cell will be solved for columns"
  (def board (create-board [1 4 7 0 0 0 0 0 0
                            2 5 8 0 0 0 0 0 0
                            3 6 9 0 0 0 0 0 0
                            4 7 1 0 0 0 0 0 0
                            5 8 2 0 0 0 0 0 0
                            6 9 3 0 0 0 0 0 0
                            7 1 4 0 0 0 0 0 0
                            8 2 5 0 0 0 0 0 0
                            9 0 0 0 0 0 0 0 0]))
  (def solved-board (into {} (:board (solve-units remove-solved-numbers-from-unit get-columns board))))
  (count solved-board) => 81
  (count (get-column-number 1 solved-board)) => 9
  (count (get-column-number 2 solved-board)) => 9
  (count (get-column-number 3 solved-board)) => 9
  (count-unsolved-cells (get-column-number 1 solved-board)) => 0
  (count-unsolved-cells (get-column-number 2 solved-board)) => 0
  (count-unsolved-cells (get-column-number 3 solved-board)) => 1
  (get-cell-numbers 9 3 solved-board) => #{1 2 3 4 5 6 7 8 9}
  (get-cell-numbers 9 2 solved-board) => #{3}
  )

(fact "only the first solvable cell will be solved for rows"
  (def board (create-board [1 2 3 4 5 6 7 8 9
                            1 2 3 4 5 6 7 8 0
                            1 2 3 4 5 6 7 8 0]))
  (def solved-board (into {} (:board (solve-units remove-solved-numbers-from-unit get-rows board))))
  (count solved-board) => 81
  (count (get-row-number 1 solved-board)) => 9
  (count (get-row-number 2 solved-board)) => 9
  (count (get-row-number 3 solved-board)) => 9
  (count-unsolved-cells (get-row-number 1 solved-board)) => 0
  (count-unsolved-cells (get-row-number 2 solved-board)) => 0
  (count-unsolved-cells (get-row-number 3 solved-board)) => 1
  (get-cell-numbers 3 9 solved-board) => #{1 2 3 4 5 6 7 8 9}
  (get-cell-numbers 2 9 solved-board) => #{9}
  )

(fact "only the first solvable cell will be solved for rows and columns and regions"
  (def board (create-board [1 2 3 4 5 6 7 8 0
                            9 4 5 0 0 0 0 0 0
                            8 7 0 0 0 0 0 0 0
                            7 0 0 0 0 0 0 0 0
                            6 0 0 0 0 0 0 0 0
                            5 0 0 0 0 0 0 0 0
                            4 0 0 0 0 0 0 0 0
                            3 0 0 0 0 0 0 0 0
                            0 0 0 0 0 0 0 0 0]))
  (count-unsolved-cells board) => 63
  (def solved-board (into {} (:board (run-solvers board remove-solved-numbers-from-unit))))
  (count solved-board) => 81
  (count (get-column-number 1 solved-board)) => 9
  (count (get-column-number 2 solved-board)) => 9
  (count (get-row-number 1 solved-board)) => 9
  (count (get-row-number 2 solved-board)) => 9
  (count-unsolved-cells solved-board) => 62;should only solve one, not bothered which
  )