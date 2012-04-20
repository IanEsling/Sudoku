(defn create-cell [x]
  (if (re-matches (re-pattern "[1-9]") (str x))
    [x]
    (vec (range 1 10))
    ))

(defn create-board [numbers]
  (map create-cell numbers)
  )

(defn random-board []
  (create-board (repeatedly 81 (partial rand-int 10)))
  )