(ns sudoku-clj.solve
  (:use clojure.set))

(defn count-cells-for [f row]
  (reduce #(+ %1 (if (f %2) 1 0)) 0 row))

(defn count-unsolved-cells [row]
  (count-cells-for #(< 1 (count %)) (for [cell row] (:numbers cell))))

(defn get-rows [board]
  (for [row (group-by :row board)] (val row)))

(defn numbers-of-solved-cells [cells]
  (remove #(< 1 (count %)) (for [cell cells] (:numbers cell))))

(defn unsolved-cells [cells]
  (filter #(< 1 (count (:numbers %))) cells))

(defn remove-solved-numbers-from-row [row]
  (def solved-numbers (apply union (numbers-of-solved-cells row)))
  (map #(case (count (:numbers %))
          1 %
          (assoc % :numbers (difference (:numbers %) solved-numbers)))
    row))

(defn remove-solved-numbers-from-board [board]
  (for [row (map #(remove-solved-numbers-from-row %) (doall (get-rows board)))
        cell row]
    cell))