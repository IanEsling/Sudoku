(ns sudoku-clj.test.board
  (:use midje.sweet)
  (:use sudoku-clj.board)
  )

(fact "board converts a list of numbers to a map of row, column and numbers"
  (create-board '(1 2 3 0 5)) => (contains [
                                             {:row 1 :column 1 :numbers #{1}}
                                             {:row 1 :column 2 :numbers #{2}}
                                             {:row 1 :column 3 :numbers #{3}}
                                             {:row 1 :column 4 :numbers #{1 2 3 4 5 6 7 8 9}}
                                             {:row 1 :column 5 :numbers #{5}}
                                             ])
  )

(fact "any thing not a number from 1 to 9 is interpreted as an unsolved cell and is converted into
      a set of 1 - 9"
  (map create-cell '(1 "a" 2)) => [#{1} #{1 2 3 4 5 6 7 8 9} #{2}]
  (map create-cell '(1 0 2)) => [#{1} #{1 2 3 4 5 6 7 8 9} #{2}]
  (map create-cell '(1 "." 2)) => [#{1} #{1 2 3 4 5 6 7 8 9} #{2}]
  (map create-cell '(1 "&" 2)) => [#{1} #{1 2 3 4 5 6 7 8 9} #{2}]
  (map create-cell '(1 [1 2 3] 2)) => [#{1} #{1 2 3 4 5 6 7 8 9} #{2}]
  )