(ns brain-sort-cljs.model
  "The goal here is to create a sorted list of items by asking the fewest questions.
  Items get placed into a sorted vector one at a time.
  A binary search is used to determine where the item should be inserted.
  Is the number of comparisons similar to the quick-sort algorithm?"
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(def fruits
  #{"cherry" "apple" "watermelon" "pineapple" "blueberry" "rockmelon" "orange" "banana"})
(def animals
  #{"dogs" "cats" "pigs" "fish" "cows" "goats" "sheep" "chickens" "rats" "turkeys"})

(def example-sets
  {"fruits" fruits
   "animals" animals})

;; While inserting, we keep track of the lower and upper index possible
;; based on answers so far.
#_ {:unsorted-set #{"apple"
                    "cherry"
                    ...}
    :sorted []
    :sorted-set #{}
    :item "prune"
    :lower-idx 0
    :upper-idx 0
    :which-is "bigger"

    :drag-sorted ["apple" ...]}

;; <lower-idx 0> apple cherry grape <upper-idx 3>
;; mid-point is 1 (cherry), prune is bigger than cherry
;; <lower-idx 0> apple cherry <upper-idx 1>
;; mid-point is 0 (apple), prune is smaller than apple
;; <lower-idx 0> apple <upper-idx 0>

(defn start-next-placement [{:keys [sorted unsorted-set] :as x}]
  (assoc x
         :item (first unsorted-set)
         :lower-idx 0
         :upper-idx (count sorted)))

(defn init [{:keys [unsorted-set] :as x}]
  (let [[a & more] unsorted-set]
    (-> x
     (assoc :sorted (if a [a] [])
            :drag-sorted (vec unsorted-set)
            :unsorted-set (set more))
     (start-next-placement))))

(defn maybe-set-item [{:keys [item unsorted-set] :as x}]
  (if (and item (contains? unsorted-set item))
    x
    (assoc x :item (second unsorted-set))))

(defn with-items [{:keys [sorted] :as x} text]
  (-> x
   (assoc :unsorted-set (set/difference
                         (set (str/split-lines text))
                         (set sorted)))
   (maybe-set-item)))

(defn vector-insert [v idx item]
  (vec (concat (take idx v) [item] (drop idx v))))

(defn mid-point [{:keys [lower-idx upper-idx]}]
  (quot (+ lower-idx upper-idx) 2))

(defn found? [{:keys [lower-idx upper-idx item] :as x}]
  (cond-> x
    (= upper-idx lower-idx)
    (-> (update :sorted vector-insert upper-idx item)
        (update :unsorted-set disj item)
        (start-next-placement))))

(defn binary-step [x k]
  (let [mid (mid-point x)]
    (-> x
        (assoc k (if (= :lower-idx k)
                   (inc mid)
                   mid))
        (found?))))

(defn vswap [{:keys [drag-sorted drag] :as x} to]
  (cond-> x
    (and drag (not= drag to))
    (-> (assoc :drag to)
        (update :drag-sorted
                (fn [y]
                  (assoc y
                         to (get drag-sorted drag)
                         drag (get drag-sorted to)))))))
