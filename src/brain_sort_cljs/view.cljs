(ns brain-sort-cljs.view
  (:require [clojure.string :as str]
            [brain-sort-cljs.model :as model]))

(defn brain [app-state]
  [:div
   [:h1 "brain-sort-cljs"]
   (into
     [:div]
     (for [[label s] model/example-sets]
       [:button
        {:on-click
         (fn [e]
           (swap! app-state assoc :unsorted-set s)
           (swap! app-state model/init))}
        label]))
   [:textarea
    {:rows 10
     :on-change
     (fn [e]
       (swap! app-state model/with-items (.-value (.-target e))))}]
   [:div
    [:h4 "Unsorted"]
    (str/join ", " (:unsorted-set @app-state))]
   [:div
    [:h4 "Sorted"]
    (str/join ", " (:sorted @app-state))]
   [:div
    [:h4 "Placing"]
    (:item @app-state)]
   [:br]
   "Which is"
   [:input
    {:on-change
     (fn [e]
       (swap! app-state assoc :which-is (.-value (.-target e))))}]
   "?"
   [:div
    [:button
     {:on-click
      (fn [e]
        (swap! app-state model/init))}
     "Start"]]
   [:div
    [:button
     {:on-click
      (fn [e]
        (swap! app-state assoc :unsorted-set (set (:sorted @app-state)))
        (swap! app-state model/init))}
     "Restart"]]
   [:div "Choose!"
    (if-let [item (:item @app-state)]
      [:div
       [:button
        {:on-click
         (fn [e]
           (swap! app-state model/binary-step :upper-idx))}
        item]
       [:button
        {:on-click
         (fn [e]
           (swap! app-state model/binary-step :lower-idx))}
        (get (:sorted @app-state) (model/mid-point @app-state))]]
      [:div "Sort complete"])]
   [:div
    [:h4 "drag n drop"]
    (into [:div]
          (for [idx (range (count (:drag-sorted @app-state)))]
            [:div
             {:style {:user-select "none"}
              :on-mouse-down
              (fn [e]
                (swap! app-state assoc :drag idx))
              :on-mouse-up
              (fn [e]
                (swap! app-state dissoc :drag))
              :on-mouse-over
              (fn [e]
                (swap! app-state model/vswap idx))}
             (get (:drag-sorted @app-state) idx)]))]])
