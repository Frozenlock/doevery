(ns doevery.core
  (:require [overtone.at-at :as at]))

(defonce pools (atom {}))

(defmacro current-namespace []
  `(str ~*ns*))

(defn create-pool
  "Create a pool for the current namespace" []
  (swap! pools assoc (current-namespace) (at/mk-pool)))

(defn get-pool
  "Get the current namespace's pool."[]
  (get @pools (current-namespace)))

(defn get-or-create-pool 
  "Get the current namespace's pool or create it."[]
  (or (get-pool)
      (do (create-pool)
          (get-pool))))

(defn do-every 
  ([ms func] (do-every ms func ""))
  ([ms func description]
     (at/every ms func (get-or-create-pool) :desc description)))

(defn show-schedule []
  (at/show-schedule (get-or-create-pool)))

(defn stop-pool
  "Stop and reset the current namespace's pool." []
  (at/stop-and-reset-pool! (get-or-create-pool) :strategy :kill))


;;;;;;;;;;;;;;;;;;;

(defn only-once-every
  "Return a function that can only be executed once every X ms.
   Only valid as long as the pool isnt' stopped."
  [ms func]
  (let [debouncer (atom nil)]
    (do-every ms #(reset! debouncer nil) (str "Execute a maximum of 1 time every "ms" ms."))
    #(when-not @debouncer
       (reset! debouncer true)
       (func))))
