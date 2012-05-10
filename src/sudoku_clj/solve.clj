(ns sudoku-clj.solve)

(defn count-cells-for [f row]
  (reduce #(+ %1 (if (f %2) 1 0)) 0 row)
  )

(defn count-solved-cells [row]
  (count-cells-for #(= 1 (count %)) row)
  )

(defn count-unsolved-cells [row]
  (count-cells-for #(< 1 (count %)) (for [cell row] (:numbers cell)))
  )

(defn solved? [row]
  (if (< 0 (count-unsolved-cells row))
    false
    true)
  )

(defn get-row-number [x board]
  (filter #(= x (:row %)) board)
  )

(defn get-rows [board]
  (partition 9 board)
  )

(defn only-solved-cells [row]
  (remove #(< 1 (count %)) (for [cell row] (:numbers cell)))
  )

(defn solve-only-possible [row]
  (apply disj (set (range 1 10))
    (for [cell (only-solved-cells row)]
      (first cell))
    )
  )

(defn solve-only-possible-in-row [board]
  (for [row (get-rows board)]
    (if (not (solved? row))
      (solve-only-possible row)
      )
    )
  )