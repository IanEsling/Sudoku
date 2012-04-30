(ns sudoku-clj.test.board
  (:use midje.sweet)
  (:use sudoku-clj.board)
  )

(fact "board converts a list of number to a list of vectors of numbers"
  (create-board '(1 2 3)) => [[1] [2] [3]])

(fact "any thing not a number from 1 to 9 is interpreted as an unsolved cell and is converted into
      a vector of 1 - 9"
  (create-board '(1 "a" 2))     => [[1] [1 2 3 4 5 6 7 8 9] [2]]
  (create-board '(1 0 2))       => [[1] [1 2 3 4 5 6 7 8 9] [2]]
  (create-board '(1 "." 2))     => [[1] [1 2 3 4 5 6 7 8 9] [2]]
  (create-board '(1 "&" 2))     => [[1] [1 2 3 4 5 6 7 8 9] [2]]
  (create-board '(1 [1 2 3] 2)) => [[1] [1 2 3 4 5 6 7 8 9] [2]]
  )