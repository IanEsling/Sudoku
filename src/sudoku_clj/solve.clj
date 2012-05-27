(ns sudoku-clj.solve
  (:use clojure.set))

(defn count-cells-for [f row]
  (reduce #(+ %1 (if (f %2) 1 0)) 0 row))

(defn count-unsolved-cells [row]
  (reduce #(+ %1 (if (< 1 (count (val %2))) 1 0)) 0 row))

(defn get-rows [board]
  (for [row (group-by #(first (key %)) board)] (into {} (val row))))

(defn numbers-of-solved-cells [cells]
  (remove #(< 1 (count %)) (for [cell cells] (val cell))))

(defn unsolved-cells [cells]
  (filter #(< 1 (count (val %))) cells))

(defn remove-solved-numbers-from-row [row]
  (def solved-numbers (apply union (numbers-of-solved-cells row)))
  (reduce (fn [newrow cell]
          (if-not (= 1 (count (second cell)))
            (assoc newrow (first cell) (difference (second cell) solved-numbers))
            newrow
            )) row row))


(defn remove-solved-numbers-from-board [board]
  (map #(remove-solved-numbers-from-row %) (doall (get-rows board)))
        )