(ns sudoku-clj.test.solvers.unique-to-unit
  (:use midje.sweet)
  (:use sudoku-clj.board)
  (:use sudoku-clj.test.solve)
  (:use sudoku-clj.solvers.unique-to-unit-solver)
  )

(fact "numbers in a row that also only exist in one region mean that number can't be in any other
cell in that region"
  (def partially-solved {[1 2] #{2 5 9} [1 5] #{2 5 9} [1 7] #{2 5 7 9} [1 9] #{2 5 7 9}})
  (def board (update-cells-in-board (create-board [1 0 3 4 0 6 0 8 0
                                                   0 0 0 0 0 0 0 0 0
                                                   0 0 0 0 0 0 0 0 0
                                                   ]) partially-solved))
  (def region-after-solving (unique-to-unit-solver (get-region-number 3 board) (get-rows board)))
  (get-cell-numbers 1 7 region-after-solving) => #{2 5 7 9}
  (get-cell-numbers 1 8 region-after-solving) => #{8}
  (get-cell-numbers 1 9 region-after-solving) => #{2 5 7 9}
  (get-cell-numbers 2 7 region-after-solving) => #{1 2 3 4 5 6 8 9}
  (get-cell-numbers 2 8 region-after-solving) => #{1 2 3 4 5 6 8 9}
  (get-cell-numbers 2 9 region-after-solving) => #{1 2 3 4 5 6 8 9}
  (get-cell-numbers 3 7 region-after-solving) => #{1 2 3 4 5 6 8 9}
  (get-cell-numbers 3 8 region-after-solving) => #{1 2 3 4 5 6 8 9}
  (get-cell-numbers 3 9 region-after-solving) => #{1 2 3 4 5 6 8 9}
  )

(fact "numbers in a region that also only exist in one row mean that number can't be in any other
cell in that row"
  (def partially-solved {[2 7] #{1 2 3 4 5 6 8 9} [2 8] #{1 2 3 4 5 6 8 9} [2 9] #{1 2 3 4 5 6 8 9} [3 7] #{1 2 3 4 5 6 8 9} [3 8] #{1 2 3 4 5 6 8 9} [3 9] #{1 2 3 4 5 6 8 9}})
  (def board (update-cells-in-board (create-board [1 0 3 4 0 6 0 8 0
                                                   0 0 0 0 0 0 0 0 0
                                                   0 0 0 0 0 0 0 0 0
                                                   ]) partially-solved))
  (def row-after-solving (unique-to-unit-solver (get-row-number 1 board) (get-regions board)))
  (get-cell-numbers 1 1 row-after-solving) => #{1}
  (get-cell-numbers 1 2 row-after-solving) => #{1 2 3 4 5 6 8 9}
  (get-cell-numbers 1 3 row-after-solving) => #{3}
  (get-cell-numbers 1 4 row-after-solving) => #{4}
  (get-cell-numbers 1 5 row-after-solving) => #{1 2 3 4 5 6 8 9}
  (get-cell-numbers 1 6 row-after-solving) => #{6}
  (get-cell-numbers 1 7 row-after-solving) => #{1 2 3 4 5 6 7 8 9}
  (get-cell-numbers 1 8 row-after-solving) => #{8}
  (get-cell-numbers 1 9 row-after-solving) => #{1 2 3 4 5 6 7 8 9}
  )

(fact "numbers in a column that also only exist in one region mean that number can't be in any other
cell in that column"
  (def partially-solved {[7 2] #{1 2 3 4 5 6 8 9} [7 3] #{1 2 3 4 5 6 8 9} [8 2] #{1 2 3 4 5 6 8 9} [8 3] #{1 2 3 4 5 6 8 9} [9 2] #{1 2 3 4 5 6 8 9} [9 3] #{1 2 3 4 5 6 8 9}})
  (def board (update-cells-in-board (create-board [1 0 0 0 0 0 0 0 0
                                                   0 0 0 0 0 0 0 0 0
                                                   3 0 0 0 0 0 0 0 0
                                                   4 0 0 0 0 0 0 0 0
                                                   0 0 0 0 0 0 0 0 0
                                                   6 0 0 0 0 0 0 0 0
                                                   0 0 0 0 0 0 0 0 0
                                                   8 0 0 0 0 0 0 0 0
                                                   0 0 0 0 0 0 0 0 0
                                                   ]) partially-solved))
  (def column-after-solving (unique-to-unit-solver (get-column-number 1 board) (get-regions board)))
  (get-cell-numbers 1 1 column-after-solving) => #{1}
  (get-cell-numbers 2 1 column-after-solving) => #{1 2 3 4 5 6 8 9}
  (get-cell-numbers 3 1 column-after-solving) => #{3}
  (get-cell-numbers 4 1 column-after-solving) => #{4}
  (get-cell-numbers 5 1 column-after-solving) => #{1 2 3 4 5 6 8 9}
  (get-cell-numbers 6 1 column-after-solving) => #{6}
  (get-cell-numbers 7 1 column-after-solving) => #{1 2 3 4 5 6 7 8 9}
  (get-cell-numbers 8 1 column-after-solving) => #{8}
  (get-cell-numbers 9 1 column-after-solving) => #{1 2 3 4 5 6 7 8 9}
  )

(fact "numbers in a column that also only exist in one region mean that number can't be in any other
cell in that region"
  (def partially-solved {[2 1] #{2 5 9} [5 1] #{2 5 9} [7 1] #{2 5 7 9} [9 1] #{2 5 7 9}})
  (def board (update-cells-in-board (create-board [1 0 0 0 0 0 0 0 0
                                                   0 0 0 0 0 0 0 0 0
                                                   3 0 0 0 0 0 0 0 0
                                                   4 0 0 0 0 0 0 0 0
                                                   0 0 0 0 0 0 0 0 0
                                                   6 0 0 0 0 0 0 0 0
                                                   0 0 0 0 0 0 0 0 0
                                                   8 0 0 0 0 0 0 0 0
                                                   0 0 0 0 0 0 0 0 0
                                                   ]) partially-solved))
  (def region-after-solving (unique-to-unit-solver (get-region-number 7 board) (get-columns board)))
  (get-cell-numbers 7 1 region-after-solving) => #{2 5 7 9}
  (get-cell-numbers 8 1 region-after-solving) => #{8}
  (get-cell-numbers 9 1 region-after-solving) => #{2 5 7 9}
  (get-cell-numbers 7 2 region-after-solving) => #{1 2 3 4 5 6 8 9}
  (get-cell-numbers 8 2 region-after-solving) => #{1 2 3 4 5 6 8 9}
  (get-cell-numbers 9 2 region-after-solving) => #{1 2 3 4 5 6 8 9}
  (get-cell-numbers 7 3 region-after-solving) => #{1 2 3 4 5 6 8 9}
  (get-cell-numbers 8 3 region-after-solving) => #{1 2 3 4 5 6 8 9}
  (get-cell-numbers 9 3 region-after-solving) => #{1 2 3 4 5 6 8 9}
  )