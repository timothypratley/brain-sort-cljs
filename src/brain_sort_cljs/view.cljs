(ns brain-sort-cljs.view
  (:require [clojure.string :as str]
            [brain-sort-cljs.model :as model]))

(defn brain [app-state]
  [:div
   [:h1 "brain-sort-cljs"]
   [:p
    "Welcome! The point of this page is to sort lists that need to be compared by a human.
    For example, prioritizing a TODO list requires you to compare priorities."]
   (into
     [:div
      [:h4 "Click on an example set of items to sort:"]]
     (for [[label s] model/example-sets]
       [:button
        {:on-click
         (fn [e]
           (swap! app-state assoc :unsorted-set s)
           (swap! app-state model/init))}
        label]))
   [:h4 "Alternatively you can type items separated by a newline in the box below:"]
   [:textarea
    {:rows 10
     :on-change
     (fn [e]
       (swap! app-state model/with-items (.-value (.-target e))))}]
   [:div
    [:h4 "Unsorted items are shown here:"]
    (str/join ", " (:unsorted-set @app-state))]
   [:div
    [:h4 "The sorted list so far is shown here:"]
    (str/join ", " (:sorted @app-state))]
   [:div
    [:h4 "Placing (if an item is being considered as to where it belongs in the list):"]
    (:item @app-state)]
   [:br]

   [:h4 "You could ask a question here like which is bigger/smaller/cuter/tastier/more important..."]
   "Which is"
   [:input
    {:on-change
     (fn [e]
       (swap! app-state assoc :which-is (.-value (.-target e))))}]
   "?"

   [:h4 "You might need these buttons to redo the exercise..."]
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
   [:h3 "This is the important bit... you need to answer the question for pairs of items."]
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
    [:h5 "And you can also sort the same set using drag and drop to compare:"]
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
