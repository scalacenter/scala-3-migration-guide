## `do`-`while` construct

The `do` keyword has acquired a different meaning in the [New Control Syntax](https://dotty.epfl.ch/docs/reference/other-new-features/control-syntax).

To avoid confusion, the traditional `do <body> while (<cond>)` construct is dropped.
It is recommended to use the equivalent `while ({ <body>; <cond> }) ()` that cross-compiles, or the new Scala 3 syntax `while { <body>; <cond> } do ()`.

The following piece of code does not compile in Scala 3.

```scala
do {
  i += 1
} while (f(i) == 0)
```

#### Scala 3 migration rewrite

When compiled with Scala 3 and the `-source:3.0-migration -rewrite` options it is rewritten into. 

```scala
while ({ {
  i += 1
} ; f(i) == 0}) ()
```

For more information you may refer to [this page](https://dotty.epfl.ch/docs/reference/dropped-features/do-while.html).
