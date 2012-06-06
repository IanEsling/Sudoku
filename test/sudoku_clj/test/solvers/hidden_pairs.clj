(ns sudoku-clj.test.solvers.hidden-pairs
  (:use midje.sweet)
  (:use sudoku-clj.board)
  (:use sudoku-clj.solvers.run)
  (:use sudoku-clj.solvers.hidden-pairs-solver)
  (:use sudoku-clj.test.solve)
  )

(fact "hidden pairs will have their numbers removed from the numbers for all cells in the same row
as well as being reduced to just the hidden pair"
  (def partially-solved {[1 5] #{5 8 9} [1 6] #{6 7 8 9} [1 7] #{6 7 8} [1 8] #{5 8 9} [1 9] #{8 9}})
  (def board (update-cells-in-board (create-board [1 2 3 4 0 0 0 0 0]) partially-solved))
  (def board-after-solving (expose-hidden-pairs-in-unit (get-row-number 1 board)))
  (get-cell-numbers 1 1 board-after-solving) => #{1}
  (get-cell-numbers 1 2 board-after-solving) => #{2}
  (get-cell-numbers 1 3 board-after-solving) => #{3}
  (get-cell-numbers 1 4 board-after-solving) => #{4}
  (get-cell-numbers 1 5 board-after-solving) => #{5 8 9}
  (get-cell-numbers 1 6 board-after-solving) => #{6 7}
  (get-cell-numbers 1 7 board-after-solving) => #{6 7}
  (get-cell-numbers 1 8 board-after-solving) => #{5 8 9}
  (get-cell-numbers 1 9 board-after-solving) => #{8 9}
  )

(fact "hidden pairs will have their numbers removed from the numbers for all cells in the same column
as well as being reduced to just the hidden pair"
  (def partially-solved {[5 5] #{1 2 3} [6 5] #{2 3 4 6} [7 5] #{1 4 6} [8 5] #{1 6} [9 5] #{1 4}})
  (def board (update-cells-in-board (create-board [1 0 0 0 9 0 0 0 0
                                                   2 0 0 0 8 0 0 0 0
                                                   3 0 0 0 7 0 0 0 0
                                                   4 0 0 0 5 0 0 0 0
                                                   5 0 0 0 0 0 0 0 0
                                                   6 0 0 0 0 0 0 0 0
                                                   7 0 0 0 0 0 0 0 0
                                                   8 0 0 0 0 0 0 0 0
                                                   9 0 0 0 0 0 0 0 0
                                                   ]) partially-solved))
  (def board-after-solving (expose-hidden-pairs-in-unit (get-column-number 5 board)))
  (get-cell-numbers 1 5 board-after-solving) => #{9}
  (get-cell-numbers 2 5 board-after-solving) => #{8}
  (get-cell-numbers 3 5 board-after-solving) => #{7}
  (get-cell-numbers 4 5 board-after-solving) => #{5}
  (get-cell-numbers 5 5 board-after-solving) => #{2 3}
  (get-cell-numbers 6 5 board-after-solving) => #{2 3}
  (get-cell-numbers 7 5 board-after-solving) => #{1 4 6}
  (get-cell-numbers 8 5 board-after-solving) => #{1 6}
  (get-cell-numbers 9 5 board-after-solving) => #{1 4}
  )

(fact "hidden pairs will have their numbers removed from the numbers for all cells in the same region
as well as being reduced to just the hidden pair"
  (def partially-solved {[5 4] #{6 7 8 9} [5 5] #{4 7 8} [5 6] #{5 7 8} [6 4] #{4 5 7} [6 5] #{4 5 7} [6 6] #{4 5 6 8 9}})
  (def board (update-cells-in-board (create-board [1 0 0 0 9 0 0 0 0
                                                   2 0 0 0 8 0 0 0 0
                                                   3 0 0 0 7 0 0 0 0
                                                   4 0 0 1 2 3 0 0 0
                                                   5 0 0 0 0 0 0 0 0
                                                   6 0 0 0 0 0 0 0 0
                                                   7 0 0 0 0 0 0 0 0
                                                   8 0 0 0 0 0 0 0 0
                                                   9 0 0 0 0 0 0 0 0
                                                   ]) partially-solved))
  (def board-after-solving (expose-hidden-pairs-in-unit (get-region-number 5 board)))
  (get-cell-numbers 4 4 board-after-solving) => #{1}
  (get-cell-numbers 4 5 board-after-solving) => #{2}
  (get-cell-numbers 4 6 board-after-solving) => #{3}
  (get-cell-numbers 5 4 board-after-solving) => #{6 9}
  (get-cell-numbers 5 5 board-after-solving) => #{4 7 8}
  (get-cell-numbers 5 6 board-after-solving) => #{5 7 8}
  (get-cell-numbers 6 4 board-after-solving) => #{4 5 7}
  (get-cell-numbers 6 5 board-after-solving) => #{4 5 7}
  (get-cell-numbers 6 6 board-after-solving) => #{6 9}
  )

(fact "more than one hidden pair will be exposed"
  (def partially-solved {[5 4] #{6 7 8} [5 5] #{5 6 7 8} [5 6] #{5 8} [6 4] #{4 5 9} [6 5] #{4 5 8 9} [6 6] #{5 8}})
  (def board (update-cells-in-board (create-board [1 0 0 0 9 0 0 0 0
                                                   2 0 0 0 8 0 0 0 0
                                                   3 0 0 0 7 0 0 0 0
                                                   4 0 0 1 2 3 0 0 0
                                                   5 0 0 0 0 0 0 0 0
                                                   6 0 0 0 0 0 0 0 0
                                                   7 0 0 0 0 0 0 0 0
                                                   8 0 0 0 0 0 0 0 0
                                                   9 0 0 0 0 0 0 0 0
                                                   ]) partially-solved))
  (def board-after-solving (expose-hidden-pairs-in-unit (get-region-number 5 board)))
  (get-cell-numbers 4 4 board-after-solving) => #{1}
  (get-cell-numbers 4 5 board-after-solving) => #{2}
  (get-cell-numbers 4 6 board-after-solving) => #{3}
  (get-cell-numbers 5 4 board-after-solving) => #{6 7}
  (get-cell-numbers 5 5 board-after-solving) => #{6 7}
  (get-cell-numbers 5 6 board-after-solving) => #{5 8}
  (get-cell-numbers 6 4 board-after-solving) => #{4 9}
  (get-cell-numbers 6 5 board-after-solving) => #{4 9}
  (get-cell-numbers 6 6 board-after-solving) => #{5 8}
  )