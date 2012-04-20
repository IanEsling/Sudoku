; only possible number left in row

(defn count-cells-for [f row]
  (reduce #(+ %1 (if (f %2) 1 0)) 0 row)
  )

(defn count-solved-cells [row]
  (count-cells-for #(= 1 (count %)) row)
  )

(defn count-unsolved-cells [row]
  (count-cells-for #(< 1 (count %)) row)
  )

(defn solved [row]
  (if (< 0 (count-unsolved-cells row))
    false
    true)
  )

(defn get-rows [board]
  (partition 9 board)
  )

(defn solve-only-possible-in-row [board]
  (for [row (get-rows board) :while (not (solved row))]
    (if (= 1 (count (unsolved-cells row))))
    (solve row)
    )
  )