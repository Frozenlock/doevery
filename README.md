# doevery

Clojure doevery X milliseconds. A small wrapper of overtone/at-at for near instant usage.

## Usage

Add `[org.clojars.frozenlock/doevery "0.1.0"]` to your `project.clj` dependencies.

Require `doevery.core` into your namespace.

```clj
(ns my-ns
	(:require [doevery.core :as d]))
```

Execute a function every 1000 ms:
```clj
(d/do-every 1000 #(println "hello from the past!") "Some test function")
```

(In Emacs, the printed string might not appear in your REPL. If it's
the case, check the nrepl-server buffer.)

Create a function that's never executed more than once every 10 seconds:
```clj
(def slow-fn (d/only-once-every 10000 #(println "I'm not printed often!")))
```
Now you can use `slow-fn` in your REPL and you won't see the printed
message more than once every 10s. You can also use it in combination
of `do-every`:


```clj
(d/do-every 1000 slow-fn)

;; This function will be called every second, but will only print
;; something every 10s.
```

To stop every recurring function:
```clj
(d/stop-pool)
```
This will also stop our `slow-fn` from being re-armed, so you'll need
to create another one if your stop the pool.

## Pool Notes

Every time `doevery` is used in a new namespace, it creates a new pool
and associates it with this namespace. This means that you can stop
everything in the same namespace using (by using `stop-pool`) without
affecting what's happening elsewhere.


## License

Copyright © 2014 Frozenlock

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
