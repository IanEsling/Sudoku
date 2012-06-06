(ns sudoku_clj.test.solvers.run
  (:use midje.sweet)
  (:use sudoku-clj.solve)
  (:use sudoku-clj.test.solve)
  (:use sudoku-clj.board)
  (:use sudoku-clj.solvers.run)
  (:use sudoku-clj.solvers.remove-solved-numbers-solver)
  (:use sudoku-clj.solvers.hidden-pairs-solver)
  (:use sudoku-clj.solvers.only-possible-number-solver)
  )

(fact "cell numbers are always sets after solving"
  ;check all numbers are still in sets
  (for [cell (filter #(= 1 (first (key %))) (into {} (:board (solve-units remove-solved-numbers-from-unit get-rows (create-board [1 2 3 4 5 6 7 8 0])))))]
    (set? (second cell))
    ) => (repeat 9 true)
  )

(fact "a solved board is not equal to the original board"
  (def board (create-board [1 2 3 4 5 6 7 8 0]))
  (into {} (:board (solve-units remove-solved-numbers-from-unit get-rows board))) =not=> (just board :in-any-order )
  )

(fact "if the solver makes no changes the new board is equal to the original board"
  (def board (create-board [1 2 3 4 5 6 7 8 9]))
  (into {} (:board (solve-units remove-solved-numbers-from-unit get-rows board))) => (just board :in-any-order )
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
  (def board-after-solving (:board (run-solvers board
                                     expose-hidden-pairs-in-unit
                                     remove-solved-numbers-from-unit
                                     only-possible-number-in-unit)))
  (count board-after-solving) => 81
  (count-unsolved-cells board-after-solving) => 58
  (get-cell-numbers 1 9 board-after-solving) => #{7}
  )
