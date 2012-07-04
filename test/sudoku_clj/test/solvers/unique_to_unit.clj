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
  (def board-after-solving (unique-to-unit-solver (get-region-number 3 board) (get-rows board)))
  (get-cell-numbers 1 7 board-after-solving) => #{2 5 7 9}
  (get-cell-numbers 1 8 board-after-solving) => #{8}
  (get-cell-numbers 1 9 board-after-solving) => #{2 5 7 9}
  (get-cell-numbers 2 7 board-after-solving) => #{1 2 3 4 5 6 8 9}
  (get-cell-numbers 2 8 board-after-solving) => #{1 2 3 4 5 6 8 9}
  (get-cell-numbers 2 9 board-after-solving) => #{1 2 3 4 5 6 8 9}
  (get-cell-numbers 3 7 board-after-solving) => #{1 2 3 4 5 6 8 9}
  (get-cell-numbers 3 8 board-after-solving) => #{1 2 3 4 5 6 8 9}
  (get-cell-numbers 3 9 board-after-solving) => #{1 2 3 4 5 6 8 9}
  )