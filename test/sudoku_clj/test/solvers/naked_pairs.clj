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