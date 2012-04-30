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
  (loop [rows (get-rows (random-board))]
    (if (seq? rows)
      (count (first rows)) => 9
      (recur (next rows))
      )
    )
  )

