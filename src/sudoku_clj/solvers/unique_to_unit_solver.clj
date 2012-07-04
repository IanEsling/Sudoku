(ns sudoku-clj.solvers.unique-to-unit-solver
  (:use sudoku-clj.board)
  (:use sudoku-clj.solve)
  (:use clojure.set)
  )

;; get unsolved intersecting cells for unit and other-unit
;; get unsolved  non-intersecting cells in unit
;; get unsolved non-intersecting cells in other-unit
;; get distinct numbers for unsolved cells in intersecting cells
;; for each number:
;;   if not exists in numbers of other-unit non-intersecting
;; unsolved cells -> remove from numbers of unsolved non-intersecting
;; cells in unit



(defn- intersecting-cells
  [unit other-unit]
  (intersection (into #{} (keys other-unit)) (into #{} (keys unit))))

(defn- numbers-of-intersecting-cells
  [unit other-unit]
  (apply union (remove (fn [numbers] (= 1 (count numbers)))
                       (map (fn [cell] (get unit cell))
                            (intersecting-cells unit other-unit)))))

(defn- non-intersecting-cells
  [unit other-unit]
  (difference (into #{} (keys unit)) (intersecting-cells unit other-unit)))

(defn- numbers-for-non-intersecting-cells
  [unit other-unit]
  (reduce (fn [found cell]
            (into found (intersection (get unit cell)
                                      (numbers-of-intersecting-cells unit other-unit))))
          #{} 
          (non-intersecting-cells unit other-unit)))

(defn non-possible-numbers
  [unit other-unit]
  (difference (numbers-of-intersecting-cells unit other-unit)
              (numbers-for-non-intersecting-cells other-unit unit)))

(defn remove-non-possible-numbers
  [unit other-unit]
  (reduce #(assoc %1 %2 (difference (get unit %2) (non-possible-numbers unit other-unit)))
          unit
          (non-intersecting-cells unit other-unit)))

(defn unique-to-unit-solver
  [unit other-units]
  (loop [other-units other-units
         new-unit unit]
    (if-not (seq (first other-units))
      new-unit
      (recur (rest other-units) (remove-non-possible-numbers new-unit (first  other-units))))))