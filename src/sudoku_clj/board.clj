(ns sudoku-clj.board)

(defn create-cell [x]
  (if (re-matches (re-pattern "[1-9]") (str x))
    (hash-set x)
    (set (range 1 10))))

(defn board-cells []
  (for [row (range 1 10)
        column (range 1 10)]
    [row column]))

(defn create-board [numbers]
  (loop [cell-numbers (map create-cell numbers)
         cells (board-cells)
         board{}]
    (if (seq cells)
      (recur (next cell-numbers) (next cells) (conj board {(first cells) (first cell-numbers)})) board)))