(ns brain-sort-cljs.main
  (:require [reagent.core :as reagent :refer [atom]]
            [goog.dom :as dom]
            [brain-sort-cljs.view :as view]))

(defonce app-state (reagent/atom {}))

(reagent/render-component
  [view/brain app-state]
  (dom/getElement "app"))

(defn on-js-reload [])
