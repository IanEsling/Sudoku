(ns sudoku-clj.test.board
  (:use midje.sweet)
  (:use sudoku-clj.board)
  )

(defn random-board
  []
  (create-board (repeatedly 81 (partial rand-int 10))))

(fact "a board can be converted into 9 rows"
  (count (get-rows (random-board))) => 9)

(fact "a board can be converted into 9 columns"
  (count (get-columns (random-board))) => 9)

(fact "a board can be converted into 9 regions"
  (count (get-regions (random-board))) => 9)

(fact "each row contains 9 cells"
  (doseq [row (get-rows (random-board))]
    (count row) => 9))

(fact "each column contains 9 cells"
  (doseq [column (get-columns (random-board))]
    (count column) => 9))

(fact "each region contains 9 cells"
  (doseq [column (get-regions (random-board))]
    (count column) => 9))

(fact "board converts a list of numbers to a map of row, column and numbers"
  (create-board '(1 2 3 0 5)) => (contains [  {[1 1] #{1}}
                                              {[1 2] #{2}}
                                              {[1 3] #{3}}
                                              {[1 4] #{1 2 3 4 5 6 7 8 9}}
                                              {[1 5] #{5}}
                                             ])
  )

(fact "any thing not a number from 1 to 9 is interpreted as an unsolved cell and is converted into
      a set of 1 - 9"
  (map create-cell-numbers '(1 "a" 2)) => [#{1} #{1 2 3 4 5 6 7 8 9} #{2}]
  (map create-cell-numbers '(1 0 2)) => [#{1} #{1 2 3 4 5 6 7 8 9} #{2}]
  (map create-cell-numbers '(1 "." 2)) => [#{1} #{1 2 3 4 5 6 7 8 9} #{2}]
  (map create-cell-numbers '(1 "&" 2)) => [#{1} #{1 2 3 4 5 6 7 8 9} #{2}]
  (map create-cell-numbers '(1 [1 2 3] 2)) => [#{1} #{1 2 3 4 5 6 7 8 9} #{2}]
  )