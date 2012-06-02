(ns sudoku_clj.test.run
  (:use midje.sweet)
  (:use sudoku-clj.solve)
  (:use sudoku-clj.test.solve)
  (:use sudoku-clj.board)
  (:use sudoku-clj.solvers.run)
  (:use sudoku-clj.solvers.remove-solved-numbers-solver)
  (:use sudoku-clj.solvers.only-possible-number-solver)
  )

(fact "the solvers should keep on running until they can't remove any more possible numbers from the unsolved cells"
  (def board (create-board [1 2 3 4 5 6 0 0 0
                            9 4 5 0 0 0 0 0 0
                            8 0 0 0 0 0 0 0 0
                            7 0 0 0 0 0 0 0 0
                            6 0 0 0 0 0 0 7 0
                            5 0 0 0 0 0 0 0 0
                            4 0 0 0 0 0 1 2 3
                            0 0 0 0 0 0 4 5 6
                            0 0 0 0 0 0 7 0 0]))
  (count-unsolved-cells board) => 59
  (def board-after-solving (:board (run-solvers board remove-solved-numbers-from-unit only-possible-number-in-unit)))
  (count board-after-solving) => 81
  (count-unsolved-cells board-after-solving) => 58
  (get-cell-numbers 1 9 board-after-solving) => #{7}
  )
