(ns sudoku-clj.solvers.hidden-pairs-solver
  (:use sudoku-clj.board)
  (:use sudoku-clj.solve)
  (:use clojure.set)
  )

(defn- all-pairs-of-unsolved-numbers [unit]
  (reduce #(conj %1 %2)
      #{}
    (for [u1 (apply union (numbers-of-unsolved-cells unit))
          u2 (filter #(not (= % u1)) (apply union (numbers-of-unsolved-cells unit)))]
        #{u1 u2})))

(defn- hidden-pairs-in-unit [unit]
  (map #(first %)
    (filter #(and ;ensure each number only exists in 2 places in this unit
               (= 2 (count (cells-containing-numbers #{(first (key %))} unit)))
               (= 2 (count (cells-containing-numbers #{(second (key %))} unit))))
      (filter #(= 2 (val %)) ;all pairs only contained by 2 cells
        (reduce #(assoc %1 %2 (count (cells-containing-numbers %2 unit)))
          {}
          (all-pairs-of-unsolved-numbers unit))))))

(defn expose-hidden-pairs-in-unit [unit]
  (if-not (< 4 (count-unsolved-cells unit)) ;can't have hidden pairs without more than 4 unsolved cells
    unit
    (loop [pairs (hidden-pairs-in-unit unit)
           new-unit unit]
      (if-let [pair (first pairs)]
        (recur (next pairs)
          (reduce #(assoc %1 (key %2) pair)
            new-unit
            (cells-containing-numbers pair new-unit)))
      new-unit))))