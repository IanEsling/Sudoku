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

(defn get-rows [board]
  (partition 9 board)
  )

(defn unsolved-rows [board]
  (filter #(not (solved? %)) (get-rows board))
  )

(defn get-row-number [x board]
  (filter #(= x (:row %)) board)
  )

(defn only-solved-cells [row]
  (remove #(< 1 (count %)) (for [cell row] (:numbers cell)))
  )

(defn get-only-possible-number [row]
  (apply disj (set (map vector (range 1 10)))
    (only-solved-cells row)
    )
  )

(defn solve-only-possible [row]
  (assoc (first (filter (comp (partial < 1) count :numbers) row))
    :numbers (first (get-only-possible-number row)))
  )

(defn only-possible-solved [row board]
  (def cell (solve-only-possible row))
  (conj (remove #(and (= (:row cell) (:row %)) (= (:column cell) (:column %))) board) cell)
  )

(defn rows-with-single-unsolved-cell [board]
  (filter #(= 1 (count-unsolved-cells %)) (get-rows board))
  )

(defn solve-only-possible-in-row [board]
  (if (< 0 (count (unsolved-rows board)))
    (only-possible-solved (first (rows-with-single-unsolved-cell board)) board)
    board)
  )

(defn remove-solved-numbers-from-row [board]
  (for [row (get-rows board)])
  )