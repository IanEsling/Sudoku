(ns sudoku-clj.test.solve
  (:use midje.sweet)
  (:use sudoku-clj.solve)
  (:use sudoku-clj.board)
  )

(defn random-board []
  (create-board (repeatedly 81 (partial rand-int 10)))
  )

(defn get-row-number [x board]
  (filter #(= x (:row %)) board)
  )

(defn get-cell [row column cells]
  (first (filter #(and (= row (:row %)) (= column (:column %))) cells))
  )

(defn solved? [row]
  (if (< 0 (count-unsolved-cells row))
    false
    true)
  )

(fact "a board can be converted into 9 rows"
  (count (get-rows (random-board))) => 9)

(fact "each row contains 9 cells"
  (doseq [rows (get-rows (random-board))]
    (count rows) => 9
    )
  )

(fact "a row is solved if all cells only have one possible number"
  (solved? (get-row-number 1 (create-board [1 2 3 4 5 6 7 8 9]))) => true
  (solved? (get-row-number 1 (create-board [1 2 3 4 5 6 7 8 0]))) => false
  (solved? (get-row-number 1 (create-board [0 2 3 4 5 6 7 8 0]))) => false
  (solved? (get-row-number 1 (create-board [0 2 3 4 5 6 7 8 9]))) => false
  )

(fact "cell numbers are always sets after solving"
  ;check all numbers are still in sets
  (for [cell (filter #(= 1 (:row %)) (remove-solved-numbers-from-board (create-board [1 2 3 4 5 6 7 8 0])))]
    (set? (:numbers cell))
    ) => (repeat 9 true)
  )

(fact "a solved board is not equal to the original board"
  (def board (create-board [1 2 3 4 5 6 7 8 0]))
(= board (remove-solved-numbers-from-board board)) => false
  )

(fact "if the solver makes no changes the new board is equal to the original board"
  (def board (create-board [1 2 3 4 5 6 7 8 9]))
  (remove-solved-numbers-from-board board) => (just board :in-any-order )
  )

(fact "an unsolved cell will have the numbers in solved cells in the same row removed"
  (def board (create-board [1 2 3 4 5 6 7 0 0]))
  (count (filter #(= 2 (count (:numbers %)))
           (filter #(= 1 (:row %))
             (remove-solved-numbers-from-row (get-row-number 1 board))
             )
           )
    ) => 2
  ;should both be a set of 8 and 9
  (:numbers (get-cell 1 8 (remove-solved-numbers-from-row (get-row-number 1 board)))) => #{8 9}
  (:numbers (get-cell 1 9 (remove-solved-numbers-from-row (get-row-number 1 board)))) => #{8 9}
  )

(fact "only the first solvable cell will be solved"
  (def board (create-board [1 2 3 4 5 6 7 8 9
                            1 2 3 4 5 6 7 8 0
                            1 2 3 4 5 6 7 8 0]))
  (def solved-board (remove-solved-numbers-from-board board))
  (count solved-board) => 81
  (count (get-row-number 1 solved-board)) => 9
  (count (get-row-number 2 solved-board)) => 9
  (count (get-row-number 3 solved-board)) => 9
  (count-unsolved-cells (get-row-number 1 solved-board)) => 0
  (count-unsolved-cells (get-row-number 2 solved-board)) => 0
  (count-unsolved-cells (get-row-number 3 solved-board)) => 1
  )