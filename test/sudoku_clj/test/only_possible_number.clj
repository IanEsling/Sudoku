(ns sudoku-clj.test.only-possible-number
  (:use midje.sweet)
  (:use sudoku-clj.board)
  (:use sudoku-clj.test.solve)
  (:use sudoku-clj.solvers.only-possible-number-solver)
  )

(defn update-cells-in-board
  [board cells]
  (reduce #(assoc %1 (key %2) (val %2)) board cells))

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