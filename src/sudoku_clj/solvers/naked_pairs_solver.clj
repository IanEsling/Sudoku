(ns sudoku-clj.solvers.naked-pairs-solver
  (:use sudoku-clj.board)
  (:use sudoku-clj.solve)
  (:use clojure.set)
  )

(defn remove-naked-pairs-in-unit
  [unit]
  (loop [pairs (keys (filter #(= 2 (count (val %))) (group-by #(val %) unit)))
         new-unit unit]
    (if-let [pair (first pairs)]
      (recur (next pairs)
        (reduce
          #(into %1 {(key %2) (if-not (= (val %2) pair) (difference (val %2) pair) (val %2))}) {} new-unit))
      new-unit)
    ))