(ns sudoku-clj.board)

(defn create-cell [x]
  (if (re-matches (re-pattern "[1-9]") (str x))
    [x]
    #(range 1 10)
    ))

(defn board-cells []
  (for [row (range 1 10)
        column (range 1 10)]
    {:row row :column column}
    )
  )

(defn create-board [numbers]
  (loop [all-cells []
         numbers (map create-cell numbers)
         cells (board-cells)]
    (if (not (nil? (:row (first cells))))
      (recur (conj all-cells {:row (:row (first cells))
                              :column (:column (first cells))
                              :numbers (first numbers)})
        (rest numbers)
        (rest cells)
        )
      all-cells
      )
    )
  )