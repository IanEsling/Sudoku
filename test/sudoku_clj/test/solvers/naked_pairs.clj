(ns sudoku-clj.test.solvers.naked-pairs
  (:use midje.sweet)
  (:use sudoku-clj.board)
  (:use sudoku-clj.solvers.run)
  (:use sudoku-clj.solvers.naked-pairs-solver)
  (:use sudoku-clj.test.solve)
  )

(fact "naked pairs will have their numbers removed from the numbers for all cells in the same row"
  (def partially-solved {[1 7] #{7 8} [1 8] #{7 8} [1 9] #{7 8 9}})
  (def board (update-cells-in-board (create-board [1 2 3 4 5 6 0 0 0]) partially-solved))
  (get-cell-numbers 1 7 (remove-naked-pairs-in-unit (get-row-number 1 board))) => #{7 8}
  (get-cell-numbers 1 8 (remove-naked-pairs-in-unit (get-row-number 1 board))) => #{7 8}
  (get-cell-numbers 1 9 (remove-naked-pairs-in-unit (get-row-number 1 board))) => #{9}
  )

(fact "naked pairs will have their numbers removed from the numbers for all cells in the same region"
  (def partially-solved {[1 3] #{1 2} [2 3] #{1 2} [3 3] #{1 2 3}})
  (def board (update-cells-in-board (create-board [9 6 0 0 0 0 0 0 0
                                                   8 5 0 0 0 0 0 0 0
                                                   7 4 0 0 0 0 0 0 0
                                                   6 0 0 0 0 0 0 0 0
                                                   5 0 0 0 0 0 0 0 0
                                                   4 0 0 0 0 0 0 0 0
                                                   0 0 0 0 0 0 0 0 0
                                                   0 0 0 0 0 0 0 0 0
                                                   0 0 0 0 0 0 0 0 0
                                                   ]) partially-solved))
  (get-cell-numbers 1 3 (remove-naked-pairs-in-unit (get-region-number 1 board))) => #{1 2}
  (get-cell-numbers 2 3 (remove-naked-pairs-in-unit (get-region-number 1 board))) => #{1 2}
  (get-cell-numbers 3 3 (remove-naked-pairs-in-unit (get-region-number 1 board))) => #{3}
  )

(fact "naked pairs will have their numbers removed from the numbers for all cells in the same column"
  (def partially-solved {[7 1] #{1 2} [8 1] #{1 2} [9 1] #{1 2 3}})
  (def board (update-cells-in-board (create-board [9 0 0 0 0 0 0 0 0
                                                   8 0 0 0 0 0 0 0 0
                                                   7 0 0 0 0 0 0 0 0
                                                   6 0 0 0 0 0 0 0 0
                                                   5 0 0 0 0 0 0 0 0
                                                   4 0 0 0 0 0 0 0 0
                                                   0 0 0 0 0 0 0 0 0
                                                   0 0 0 0 0 0 0 0 0
                                                   0 0 0 0 0 0 0 0 0
                                                   ]) partially-solved))
  (get-cell-numbers 7 1 (remove-naked-pairs-in-unit (get-column-number 1 board))) => #{1 2}
  (get-cell-numbers 8 1 (remove-naked-pairs-in-unit (get-column-number 1 board))) => #{1 2}
  (get-cell-numbers 9 1 (remove-naked-pairs-in-unit (get-column-number 1 board))) => #{3}
  )

(fact "removing naked pairs from cells will stop when the first cell has been solved"
  (def partially-solved {[3 4] #{7 8} [3 5] #{7 8 9} [3 6] #{7 8}
                         [4 7] #{1 2} [4 8] #{1 2} [4 9] #{1 2 3}
                         [7 3] #{5 7 8} [8 3] #{5 7} [9 3] #{5 7}})
  (def board (update-cells-in-board (create-board [0 0 9 1 2 3 0 0 0
                                                   8 0 1 4 5 6 0 0 0
                                                   0 0 2 0 0 0 0 0 0
                                                   4 5 6 7 8 9 0 0 0
                                                   5 0 3 0 0 0 0 0 0
                                                   4 0 4 0 0 0 0 0 0
                                                   0 0 0 0 0 0 0 0 0
                                                   0 0 0 0 0 0 0 0 0
                                                   0 0 0 0 0 0 0 0 0
                                                   ]) partially-solved))
  (count-unsolved-cells board) => 61
  (def board-after-solving (into {} (:board (run-solvers board remove-naked-pairs-in-unit))))
  (count board-after-solving) => 81
  (count-unsolved-cells board-after-solving) => 60;should only solve one, not bothered which)
  )