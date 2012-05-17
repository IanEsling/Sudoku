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

(defn get-cell [x y cells]
  (filter #(and (= x (:row %)) (= y (:column %))) cells)
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
  )

(fact "a row with one unsolved cell left can be solved"
  ;check there's no cells in row 1 with more than one possible number after we've solved it
  (count (filter #(< 1 (count (:numbers %)))
           (filter #(= 1 (:row %))
             (solve-only-possible-in-row (create-board [1 2 3 4 5 6 7 8 0]))
             )
           )
    ) => 0
  ;check all numbers are still in sets
  (for [cell (filter #(= 1 (:row %)) (solve-only-possible-in-row (create-board [1 2 3 4 5 6 7 8 0])))]
    (set? (:numbers cell))
    ) => (repeat 9 true)
  ;check board is different
  (def board (create-board [1 2 3 4 5 6 7 8 0]))
  (= board (solve-only-possible-in-row board)) => false
  ;check board the same if nothing solvable
  (def board (create-board [1 2 3 4 5 6 7 8 9]))
  (= board (solve-only-possible-in-row board)) => true
  )

(fact "a row will only be solved if only one cell is left unsolved"
  ;check there's still 2 cells in row 1 with more than one possible number after we've solved it
  (count (filter #(= 9 (count (:numbers %)))
           (filter #(= 1 (:row %))
             (solve-only-possible-in-row (create-board [1 2 3 4 5 6 7 0 0]))
             )
           )
    ) => 2
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
  (:numbers (first (filter #(and (= 1 (:row %)) (= 8 (:column %))) (get-row-number 1 (remove-solved-numbers-from-row (get-row-number 1 board)))))) => #{8 9}
  (:numbers (first (filter #(and (= 1 (:row %)) (= 9 (:column %))) (get-row-number 1 (remove-solved-numbers-from-row (get-row-number 1 board)))))) => #{8 9}
  )

(fact "only the first solvable cell will be solved"
  (def board (create-board [1 2 3 4 5 6 7 8 0 1 2 3 4 5 6 7 8 0]))
  (def solved-board (remove-solved-numbers-from-board board))
(println "solved board:")
(println solved-board)
  (count (get-row-number 1 solved-board)) => 9
  (count (filter #(< 1 (count (:numbers %)))
           (filter #(= 1 (:row %))
             solved-board)
           )
    ) => 0
  (count (filter #(< 1 (count (:numbers %)))
           (filter #(= 2 (:row %))
             solved-board)
           )
    ) => 1
  )