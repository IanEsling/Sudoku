(ns sudoku-clj.test.only-possible-number
  (:use midje.sweet)
  (:use sudoku-clj.board)
  (:use sudoku-clj.test.solve)
  (:use sudoku-clj.solvers.only-possible-number-solver)
  )

(fact "if an unsolved cell is the only one in a unit that could be a particular number then that
cell must be that number"
  (def partially-solved {[1 8] #{7 8 9} [1 9] #{7 9}})
  (def board (reduce #(assoc %1 (key %2) (val %2)) (create-board [1 2 3 4 5 6 7 0 0 0 7 6 3 2 1 5 9 0]) partially-solved))
  (get-cell-numbers 1 8 (only-possible-number-in-unit (get-row-number 1 board))) => #{8}
  )
;
;(fact "an unsolved cell will have the numbers in solved cells in the same row removed"
;  (def board (create-board [1 2 3 4 5 6 7 0 0]))
;  (count (filter #(= 2 (count (second %)))
;           (into {} (filter #(= 1 (first (key %)))
;                      (into {} (remove-solved-numbers-from-unit (get-row-number 1 board))))))) => 2
;  ;should both be a set of 8 and 9
;  (get-cell-numbers 1 8 (remove-solved-numbers-from-unit (get-row-number 1 board))) => #{8 9}
;  (get-cell-numbers 1 9 (remove-solved-numbers-from-unit (get-row-number 1 board))) => #{8 9}
;  )