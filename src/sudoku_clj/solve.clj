(ns sudoku-clj.solve
  (:use clojure.set))

(defn count-unsolved-cells
  [cells]
  (reduce #(+ %1 (if (< 1 (count (val %2))) 1 0)) 0 cells))

(defn get-rows
  [board]
  (for [row (group-by #(first (key %)) board)] (into {} (val row))))

(defn get-columns
  [board]
  (for [column (group-by #(second (key %)) board)] (into {} (val column))))

(defn get-region-cells
  [f-x-range f-y-range board]
  (fn
    [newboardseq]
    (def newboard {})
    (conj newboardseq (into {} (for [x (f-x-range) y (f-y-range)]
                                 (assoc newboard [x y] (get board [x y])))))))

(defn get-regions
  [board]
  ((comp ;order is important here for logical numbering of regions (i.e. number 1 is top left, 3 is top right etc)
     (get-region-cells (partial range 1 4) (partial range 1 4) board)
     (get-region-cells (partial range 1 4) (partial range 4 7) board)
     (get-region-cells (partial range 1 4) (partial range 7 10) board)
     (get-region-cells (partial range 4 7) (partial range 1 4) board)
     (get-region-cells (partial range 4 7) (partial range 4 7) board)
     (get-region-cells (partial range 4 7) (partial range 7 10) board)
     (get-region-cells (partial range 7 10) (partial range 1 4) board)
     (get-region-cells (partial range 7 10) (partial range 4 7) board)
     (get-region-cells (partial range 7 10) (partial range 7 10) board)
     )
    (seq {})))

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
    (def newboard (remove-solved-numbers board-to-solve))
    (if-not (= count-unsolved-before (reduce #(+ %1 (count %2)) 0 (numbers-of-unsolved-cells newboard)))
      (recur (reduce #(+ %1 (count %2)) 0 (numbers-of-unsolved-cells newboard)) newboard)
    newboard)))