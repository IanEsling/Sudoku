(ns sudoku-clj.solve
  (:use clojure.set))

(defn count-cells-for [f row]
  (reduce #(+ %1 (if (f %2) 1 0)) 0 row)
  )

(defn count-unsolved-cells [row]
  (count-cells-for #(< 1 (count %)) (for [cell row] (:numbers cell)))
  )

(defn get-rows [board]
  (for [row (group-by :row board)] (val row))
  )

(defn numbers-of-solved-cells [row]
  (remove #(< 1 (count %)) (for [cell row] (:numbers cell)))
  )

(defn unsolved-cells [row]
  (filter #(< 1 (count (:numbers %))) row)
  )

(defn remove-solved-numbers-from-row [row]
  (def solved-numbers (apply union (numbers-of-solved-cells row)))
  (loop [cells (unsolved-cells row)
         new-row row]
    (def cell (first cells))
    (if (not (nil? cell))
      (recur (next cells)
        (conj (remove #(and (= (:row cell) (:row %)) (= (:column cell) (:column %))) new-row)
          (assoc cell :numbers (difference (:numbers cell) solved-numbers)
            )
          )
        )
      new-row)
    )
  )

(defn remove-solved-numbers-from-board [board]
  (loop [rows (get-rows board)
         newboard board]
    (doall newboard)
    (def new-row (remove-solved-numbers-from-row (first rows)))
    (if (and (not (nil? new-row)) (not (= (count-unsolved-cells (first rows)) (count-unsolved-cells new-row))))
      (apply conj (remove #(= (first (keys (group-by :row new-row))) (:row %)) newboard)
        new-row)
      (if (and (not (nil? new-row)) (= (count-unsolved-cells (first rows)) (count-unsolved-cells new-row)))
        (recur (next rows) (apply conj (remove #(= (first (keys (group-by :row new-row))) (:row %)) newboard)
                             new-row))
        newboard))))