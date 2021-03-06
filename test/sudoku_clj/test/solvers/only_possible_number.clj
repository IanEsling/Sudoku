(ns sudoku-clj.test.solvers.only-possible-number
  (:use midje.sweet)
  (:use sudoku-clj.board)
  (:use sudoku-clj.test.solve)
  (:use sudoku-clj.solvers.run)
  (:use sudoku-clj.solvers.only-possible-number-solver)
  )

(fact "if an unsolved cell is the only one in a row that could be a particular number then that
cell must be that number"
  (def partially-solved {[1 8] #{7 8 9} [1 9] #{7 9}})
  (def board (update-cells-in-board (create-board [1 2 3 4 5 6 7 0 0 0 7 6 3 2 1 5 9 0]) partially-solved))
  (get-cell-numbers 1 8 (only-possible-number-in-unit (get-row-number 1 board))) => #{8}
  (get-cell-numbers 1 9 (only-possible-number-in-unit (get-row-number 1 board))) => #{7 9}
  )

(fact "if an unsolved cell is the only one in a region that could be a particular number then that
cell must be that number"
  (def partially-solved {[3 4] #{7 8 9} [3 5] #{7 8} [3 6] #{7 8}})
  (def board (update-cells-in-board (create-board [1 2 3 4 5 6 7 0 0
                                                   0 4 5 1 2 3 9 0 0
                                                   0 6 0 0 0 0 0 0 0
                                                   ]) partially-solved))
  (get-cell-numbers 3 4 (only-possible-number-in-unit (get-region-number 2 board))) => #{9}
  (get-cell-numbers 3 5 (only-possible-number-in-unit (get-region-number 2 board))) => #{7 8}
  (get-cell-numbers 3 6 (only-possible-number-in-unit (get-region-number 2 board))) => #{7 8}
  )

(fact "if an unsolved cell is the only one in a column that could be a particular number then that
cell must be that number"
  (def partially-solved {[7 5] #{1 2} [8 5] #{1 2 3} [9 5] #{1 2}})
  (def board (update-cells-in-board (create-board [1 2 3 4 5 6 7 0 0
                                                   0 4 5 1 8 3 9 0 0
                                                   0 6 0 0 9 0 0 0 0
                                                   0 6 0 0 7 0 0 0 0
                                                   0 6 0 0 4 0 0 0 0
                                                   0 6 0 0 6 0 0 0 0
                                                   0 6 0 0 0 0 0 0 0
                                                   0 6 0 0 0 0 0 0 0
                                                   0 6 0 0 0 0 0 0 0
                                                   ]) partially-solved))
  (get-cell-numbers 7 5 (only-possible-number-in-unit (get-column-number 5 board))) => #{1 2}
  (get-cell-numbers 8 5 (only-possible-number-in-unit (get-column-number 5 board))) => #{3}
  (get-cell-numbers 9 5 (only-possible-number-in-unit (get-column-number 5 board))) => #{1 2}
  )

(fact "only the first solvable cell will be solved for rows"
  (def partially-solved {[1 7] #{7 8 9} [1 8] #{7 8} [1 9] #{7 8 9}
                         [2 1] #{1 4} [2 6] #{1 4 8} [2 9] #{1 4}
                         [3 3] #{1 2 7} [3 8] #{1 2} [3 9] #{1 2}})
  (def board (update-cells-in-board (create-board [1 2 3 4 5 6 0 0 0
                                                   0 7 6 3 2 0 5 9 0
                                                   9 8 0 6 5 4 3 0 0
                                                   ]) partially-solved))

  (def board-after-solving (into {} (:board ((partial solve-units only-possible-number-in-unit) get-rows board))))
  (count board-after-solving) => 81
  (count (get-row-number 1 board-after-solving)) => 9
  (count (get-row-number 2 board-after-solving)) => 9
  (count (get-row-number 3 board-after-solving)) => 9
  (count-unsolved-cells (get-row-number 1 board-after-solving)) => 3
  (count-unsolved-cells (get-row-number 2 board-after-solving)) => 2
  (count-unsolved-cells (get-row-number 3 board-after-solving)) => 3
  (get-cell-numbers 1 7 board-after-solving) => #{7 8 9}
  (get-cell-numbers 1 8 board-after-solving) => #{7 8}
  (get-cell-numbers 1 9 board-after-solving) => #{7 8 9}
  (get-cell-numbers 2 1 board-after-solving) => #{1 4}
  (get-cell-numbers 2 6 board-after-solving) => #{8}
  (get-cell-numbers 2 9 board-after-solving) => #{1 4}
  (get-cell-numbers 3 3 board-after-solving) => #{1 2 7}
  (get-cell-numbers 3 8 board-after-solving) => #{1 2}
  (get-cell-numbers 3 9 board-after-solving) => #{1 2}
  )

(fact "only the first solvable cell will be solved for columns"
  (def partially-solved {[7 1] #{7 8 9} [8 1] #{7 8} [9 1] #{7 8 9}
                         [1 2] #{1 4} [5 2] #{1 4 8} [8 2] #{1 4}
                         [4 4] #{1 2 7} [5 4] #{1 2} [9 4] #{1 2}})
  (def board (update-cells-in-board (create-board [1 0 9 7 5 6 0 0 0
                                                   2 4 8 9 2 0 5 9 0
                                                   3 5 7 8 5 4 3 0 0
                                                   4 6 6 0 5 4 3 0 0
                                                   5 0 5 0 5 4 3 0 0
                                                   6 7 4 1 5 4 3 0 0
                                                   0 8 3 2 5 4 3 0 0
                                                   0 0 2 3 5 4 3 0 0
                                                   0 9 1 0 5 4 3 0 0
                                                   ]) partially-solved))

  (def board-after-solving (into {} (:board ((partial solve-units only-possible-number-in-unit) get-columns board))))
  (count board-after-solving) => 81
  (count (get-column-number 1 board-after-solving)) => 9
  (count (get-column-number 2 board-after-solving)) => 9
  (count (get-column-number 4 board-after-solving)) => 9
  (count-unsolved-cells (get-column-number 1 board-after-solving)) => 3
  (count-unsolved-cells (get-column-number 2 board-after-solving)) => 2
  (count-unsolved-cells (get-column-number 4 board-after-solving)) => 3
  (get-cell-numbers 7 1 board-after-solving) => #{7 8 9}
  (get-cell-numbers 8 1 board-after-solving) => #{7 8}
  (get-cell-numbers 9 1 board-after-solving) => #{7 8 9}
  (get-cell-numbers 1 2 board-after-solving) => #{1 4}
  (get-cell-numbers 5 2 board-after-solving) => #{8}
  (get-cell-numbers 8 2 board-after-solving) => #{1 4}
  (get-cell-numbers 4 4 board-after-solving) => #{1 2 7}
  (get-cell-numbers 5 4 board-after-solving) => #{1 2}
  (get-cell-numbers 9 4 board-after-solving) => #{1 2}
  )

(fact "only the first solvable cell will be solved for regions"
  (def partially-solved {[1 5] #{7 8 9} [2 6] #{7 8} [3 4] #{7 8 9}
                         [4 3] #{1 4} [5 2] #{1 4 8} [6 1] #{1 4}
                         [7 5] #{1 2 7} [7 6] #{1 2} [9 4] #{1 2}})
  (def board (update-cells-in-board (create-board [1 0 9 7 0 6 0 0 0
                                                   2 4 8 9 2 0 5 9 0
                                                   3 5 7 0 5 4 3 0 0
                                                   4 6 0 0 5 4 3 0 0
                                                   5 0 5 0 5 4 3 0 0
                                                   0 7 4 1 5 4 3 0 0
                                                   0 8 3 2 0 0 3 0 0
                                                   0 0 2 3 5 4 3 0 0
                                                   0 9 1 0 5 4 3 0 0
                                                   ]) partially-solved))

  (def board-after-solving (into {} (:board ((partial solve-units only-possible-number-in-unit) get-regions board))))
  (count board-after-solving) => 81
  (count (get-region-number 2 board-after-solving)) => 9
  (count (get-region-number 4 board-after-solving)) => 9
  (count (get-region-number 8 board-after-solving)) => 9
  (count-unsolved-cells (get-region-number 2 board-after-solving)) => 3
  (count-unsolved-cells (get-region-number 4 board-after-solving)) => 2
  (count-unsolved-cells (get-region-number 8 board-after-solving)) => 3
  (get-cell-numbers 1 5 board-after-solving) => #{7 8 9}
  (get-cell-numbers 2 6 board-after-solving) => #{7 8}
  (get-cell-numbers 3 4 board-after-solving) => #{7 8 9}
  (get-cell-numbers 4 3 board-after-solving) => #{1 4}
  (get-cell-numbers 5 2 board-after-solving) => #{8}
  (get-cell-numbers 6 1 board-after-solving) => #{1 4}
  (get-cell-numbers 7 5 board-after-solving) => #{1 2 7}
  (get-cell-numbers 7 6 board-after-solving) => #{1 2}
  (get-cell-numbers 9 4 board-after-solving) => #{1 2}
  )

(fact "only the first solvable cell will be solved for rows and columns and regions"
  (def partially-solved {[3 4] #{8 9} [3 8] #{7 8} [3 9] #{8 9}
                         [1 2] #{1 4} [5 2] #{1 4 8} [5 8] #{1 4}
                         [7 5] #{1 2 7} [7 6] #{1 2} [9 4] #{1 2}})
  (def board (update-cells-in-board (create-board [1 0 9 7 0 6 0 0 0
                                                   2 4 8 9 2 0 5 9 0
                                                   3 5 7 0 5 4 3 0 0
                                                   4 6 0 0 5 4 3 0 0
                                                   5 0 5 0 5 4 3 0 0
                                                   0 7 4 1 5 4 3 0 0
                                                   0 8 3 2 0 0 3 0 0
                                                   0 0 2 3 5 4 3 0 0
                                                   0 9 1 0 5 4 3 0 0
                                                   ]) partially-solved))
  (count-unsolved-cells board) => 34
  (def board-after-solving (into {} (:board (run-solvers board only-possible-number-in-unit))))
  (count board-after-solving) => 81
  (count (get-column-number 1 board-after-solving)) => 9
  (count (get-column-number 2 board-after-solving)) => 9
  (count (get-row-number 1 board-after-solving)) => 9
  (count (get-row-number 2 board-after-solving)) => 9
  (count-unsolved-cells board-after-solving) => 33;should only solve one, not bothered which
  )