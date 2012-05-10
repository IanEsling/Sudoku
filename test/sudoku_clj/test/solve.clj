(ns sudoku-clj.test.solve
  (:use midje.sweet)
  (:use sudoku-clj.solve)
  (:use sudoku-clj.board)
  (:use clojure.core)
  )

(defn random-board []
  (create-board (repeatedly 81 (partial rand-int 10)))
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
;check we can find row 1
  (count (filter #(= 1 (:row %))
           (solve-only-possible-in-row (create-board [1 2 3 4 5 6 7 8 0]))
           )) => 1
;check there's no cells with more than one possible number
  (count (filter #(< 1 (count (:numbers %)))
           (filter #(= 1 (:row %))
             (solve-only-possible-in-row (create-board [1 2 3 4 5 6 7 8 0]))
             )
           )
    ) => 0
  )
