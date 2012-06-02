(ns sudoku-clj.solvers.only-possible-number-solver
  (:use sudoku-clj.board)
  (:use sudoku-clj.solve)
  (:use clojure.set)
  )

(defn map-of-unsolved-numbers
  [unit]
  (reduce #(into %1 {%2 0}) {} (apply union (numbers-of-unsolved-cells unit)))
  )

(defn frequencies-of-unsolved-numbers
  [unit]
  (reduce #(reduce (fn [t x] (assoc t x (+ 1 (get t x)))) %1 %2)
    (map-of-unsolved-numbers unit)
    (numbers-of-unsolved-cells unit))
  )

(defn only-possible-number-in-unit
  [unit]
  (def freq (into {} (filter #(= 1 (val %)) (frequencies-of-unsolved-numbers unit))))
  (reduce (fn
            [newunit cell]
            (if (= 1 (count (intersection (val cell) (set (keys freq)))))
              (into newunit {(first cell) (set (intersection (val cell) (set (keys freq))))})
              (into newunit {(first cell) (second cell)}))
            ) {} unit))

(defn solve-only-possible-numbers-by-unit
  ([f-get-unit board]
    (loop [units (f-get-unit board)
           newboard {}
           solved false]
      (if-let [unit (first units)]
        (if-not solved
          (let [solved-before (count (numbers-of-solved-cells unit))
                unit-after-solving (only-possible-number-in-unit unit)
                solved-after (count (numbers-of-solved-cells unit-after-solving))]
            (recur (next units) (conj newboard unit-after-solving) (< solved-before solved-after)))
          (recur (next units) (conj newboard unit) true))
        (assoc {:solved solved} :board newboard)))))

(defn solver-function
  [f]
  (fn [board-map]
    (if-let [solved (:solved board-map)]
      board-map
      (solve-only-possible-numbers-by-unit f (:board board-map)))))

(defn solve-only-possible-numbers
  [board]
  ((comp (solver-function get-regions) (solver-function get-columns) (solver-function get-rows))
    (assoc {:solved false} :board board)))