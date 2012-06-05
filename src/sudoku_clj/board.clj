(ns sudoku-clj.board)

(defn- get-region-cells
  [f-x-range f-y-range board]
  (fn
    [newboardseq]
    (def regions-board {})
    (conj newboardseq (into {} (for [x (f-x-range) y (f-y-range)]
                                 (assoc regions-board [x y] (get board [x y])))))))

(defn- create-cell-numbers
  [x]
  (if (re-matches (re-pattern "[1-9]") (str x))
    (hash-set x)
    (set (range 1 10))))

(defn- board-cells
  []
  (for [row (range 1 10)
        column (range 1 10)]
    [row column]))

(defn count-unsolved-cells
  [cells]
  (reduce #(+ %1 (if (< 1 (count (val %2))) 1 0)) 0 cells))

(defn get-rows
  [board]
  (for [row (group-by #(first (key %)) board)] (into {} (val row))))

(defn get-columns
  [board]
  (for [column (group-by #(second (key %)) board)] (into {} (val column))))

(defn get-regions
  [board]
  ((comp ;order is important here for logical numbering of regions (i.e. number 1 is top left, 3 is top right etc)
     (get-region-cells (partial range 1 4) (partial range 1 4) board)
     (get-region-cells (partial range 1 4) (partial range 4 7) board)
     (get-region-cells (partial range 1 4) (partial range 7 10) board)
     (get-region-cells (partial range 4 7) (partial range 1 4) board)
     (get-region-cells (partial range 4 7) (partial range 4 7) board)
     (get-region-cells (partial range 4 7) (partial range 7 10) board)
     (get-region-cells (partial range 7 10) (partial range 1 4) board)
     (get-region-cells (partial range 7 10) (partial range 4 7) board)
     (get-region-cells (partial range 7 10) (partial range 7 10) board)
     )
    (seq {})))

(defn create-board
  [starting-numbers]
  (loop [cell-numbers (map create-cell-numbers starting-numbers)
         cells (board-cells)
         board{}]
    (if (seq cells)
      (recur (next cell-numbers) (next cells) (conj board {(first cells) (first cell-numbers)}))
      board)))