(ns sudoku-clj.solve
  (:use clojure.set))

(defn count-unsolved-cells [row]
  (reduce #(+ %1 (if (< 1 (count (val %2))) 1 0)) 0 row))

(defn get-rows [board]
  (for [row (group-by #(first (key %)) board)] (into {} (val row))))

(defn numbers-of-solved-cells [cells]
  (remove #(< 1 (count %)) (for [cell cells] (val cell))))

(defn remove-solved-numbers-from-row [row]
  (def solved-numbers (apply union (numbers-of-solved-cells row)))
  (reduce (fn [newrow cell]
            (if-not (= 1 (count (second cell)))
              (assoc newrow (first cell) (difference (second cell) solved-numbers))
              newrow
              ))
    row row))

(defn remove-solved-numbers-from-board [board]
  (loop [rows (get-rows board)
         newboard {}
         solved false]
    (if-let [row (first rows)]
        (if-not solved
          (let [solved-before (count (numbers-of-solved-cells row))
                row-after-solving (remove-solved-numbers-from-row row)
                solved-after (count (numbers-of-solved-cells row-after-solving))]
            (recur (next rows) (conj newboard row-after-solving) (< solved-before solved-after)))
          (recur (next rows) (conj newboard row) true))
      newboard)))
