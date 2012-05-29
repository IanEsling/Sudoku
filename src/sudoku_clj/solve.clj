(ns sudoku-clj.solve
  (:use clojure.set)
  (:use sudoku-clj.board))

(defn numbers-of-solved-cells
  [cells]
  (remove #(< 1 (count %)) (for [cell cells] (val cell))))

(defn numbers-of-unsolved-cells
  [cells]
  (remove #(= 1 (count %)) (for [cell cells] (val cell))))

(defn remove-solved-numbers-from-unit
  [unit]
  (def solved-numbers (apply union (numbers-of-solved-cells unit)))
  (reduce (fn [newunit cell]
            (if-not (= 1 (count (second cell)))
              (assoc newunit (first cell) (difference (second cell) solved-numbers))
              newunit
              ))
    unit unit))

(defn remove-solved-numbers-from-board-by-unit
  ([f-get-unit board]
    (loop [rows (f-get-unit board)
           newboard {}
           solved false]
      (if-let [row (first rows)]
        (if-not solved
          (let [solved-before (count (numbers-of-solved-cells row))
                row-after-solving (remove-solved-numbers-from-unit row)
                solved-after (count (numbers-of-solved-cells row-after-solving))]
            (recur (next rows) (conj newboard row-after-solving) (< solved-before solved-after)))
          (recur (next rows) (conj newboard row) true))
        (assoc {:solved solved} :board newboard)))))

(defn solver-function
  [f]
  (fn [board-map]
    (if-let [solved (:solved board-map)]
      board-map
      (remove-solved-numbers-from-board-by-unit f (:board board-map)))))

(defn remove-solved-numbers
  [board]
  ((comp (solver-function get-regions) (solver-function get-columns) (solver-function get-rows))
    (assoc {:solved false} :board board)))

(defn run-solvers
  [board]
  (loop [count-unsolved-before (reduce #(+ %1 (count %2)) 0 (numbers-of-unsolved-cells board))
         board-to-solve board]
    (def solved-board (:board (remove-solved-numbers board-to-solve)))
    (if-not (= count-unsolved-before (reduce #(+ %1 (count %2)) 0 (numbers-of-unsolved-cells solved-board)))
      (recur (reduce #(+ %1 (count %2)) 0 (numbers-of-unsolved-cells solved-board)) solved-board)
      solved-board)))