(ns sudoku-clj.solve
  (:use clojure.set))

(defn count-cells-for [f row]
  (reduce #(+ %1 (if (f %2) 1 0)) 0 row)
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

(defn numbers-of-solved-cells [row]
  (remove #(< 1 (count %)) (for [cell row] (:numbers cell)))
  )

(defn get-only-possible-number [row]
  (apply disj (set (range 1 10))
    (apply union (numbers-of-solved-cells row))
    )
  )

(defn unsolved-cells [row]
  (filter (comp (partial < 1) count :numbers ) row)
  )

(defn solve-only-possible [row]
  (assoc (first (unsolved-cells row))
    :numbers (get-only-possible-number row))
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

(defn remove-solved-numbers-from-row [row board]
  (def solved-numbers (apply union (numbers-of-solved-cells row)))
  (loop [cells (unsolved-cells row)
         newboard board]
    (def cell (first cells))
    (if (not (nil? cell))
      (recur (next cells) (conj (remove #(and (= (:row cell) (:row %)) (= (:column cell) (:column %))) newboard)
                            (assoc cell
                            :numbers (difference (:numbers cell) solved-numbers))
        )
      )
      newboard
      )
    )
  )