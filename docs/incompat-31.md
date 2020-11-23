---
id: incompat-31
title: Dotty 3.1-migration Mode
---
## Looking ahead to Scala 3.1

Some deprecations have been postponed to 3.1 to facilitate the migration from 2.13 to 3.0 and then from 3.0 to 3.1.
However some of the migration rules are already available and you are likely to be able to apply them in your codebase.
In this way you get accustomed to the new syntax and prepared for 3.1.

Beware though that some of the `3.1-migration` rewrites break the source compatibility with Scala 2.13.

### Rule 1 - Replace wildcard type argument '_' with '?'

The deprecation of the `_` syntax for wildcard type arguments has be postponed to Scala 3.1 and it will be replaced by the `?` syntax. You can find the motivation for this in the [Dotty reference](https://dotty.epfl.ch/docs/reference/changed-features/wildcards.html).

However there already is a migration rule that you can apply using some slightly different options than before: `dotc -source:3.1-migration -rewrite`. Note the `3.1` version mentioned here.

For example:

```scala
def compare(x: Container[_], y: Container[_]): Int = {
  x.weight - y.weight
}
```

Is rewritten into

```scala
def compare(x: Container[?], y: Container[?]): Int = {
  x.weight - y.weight
}
```

### Rule 2 - Add Parentheses around the implicit parameter of a lambda

*Will be available in Dotty 0.26.0 or Dotty 0.27.0-RC1*

Starting from Scala 3.1, it will be required to enclose the implicit parameter of a lambda in parentheses, making the following Scala 3 code illegal.

```scala
val f = { implicit x: Context => ??? }
```

Compiling with `dotc -source:3.1-migration -rewrite` rewrites it into:

```scala
val f = { (implicit x: Context) => ??? }
```

### Rule 3 (deprecation) - Backquote alphanumeric methods used as infix operator

Starting from Scala 3.1, alphanumeric methods should be annotated with `@infix` to be used as infix operators (see [Dotty documentation](https://dotty.epfl.ch/docs/reference/changed-features/operators.html#the-infix-annotation)).
The `-deprecation` mode of the compiler will warn you foreach infix call of un-annotated methods.

Here, the call of the `difference` method is deprecated:

```scala
trait MultiSet {
  def difference(other: MultiSet): MultiSet
}

def test(s1: MultiSet, s2: MultiSet): MultiSet = 
  s1 difference s2
```

The compiler can backquote the method call under the `-source:3.1 -deprecation -rewrite` options.

```scala
trait MultiSet {
  def difference(other: MultiSet): MultiSet
}

def test(s1: MultiSet, s2: MultiSet): MultiSet = 
  s1 `difference` s2
```

### Rule 4 - Unchecked pattern bindings

*Will be available in Dotty 0.26.0 or Dotty 0.27.0-RC1*

From Scala 3.1 on, pattern binding will require to be type consistent in order to prevent undesired runtime errors.
See [Dotty documentation](https://dotty.epfl.ch/docs/reference/changed-features/pattern-bindings.html) for more information on this.

This piece of code compiles in Scala 2.13 and Scala 3.0 but not in Scala 3.1:

```scala
val list: List[Int] = List(1)
val head :: _ = list
```

You can use the `@unchecked` annotation to tell the compiler to ignore that the binding can fail.
Compiling with `dotc -source:3.1-migration -rewrite` can write it automatically.

```scala
val list: List[Int] = List(1)
val head :: _: @unchecked = list
```

Similarly, in a `for` expression, this piece of code compiles in Scala 2.13 and Scala 3.0 but not in Scala 3.1:

```scala
val listOpt: List[Option[Int]] = List(Some(1), None)
for (Some(value) <- listOpt) println(value)
```

In Scala 2 and Scala 3.0, the elements of `listOpt` are filtered to retain only the value of type `Some` .

In Scala 3.1, this syntax does not induce filtering, but the binding is type checked to prevent runtime errors.
You can still have the same behavior than Scala 2 by adding the `case` keyword.
Compiling with `dotc -source:3.1-migration -rewrite` can add it for you automatically.

```scala
val listOpt: List[Option[Int]] = List(Some(1), None)
for (case Some(value) <- listOpt) println(value)
```

### Rule 5 - Remove method value syntax for eta-expansion

*This rule breaks the source compatibility with Scala 2.13.*

The method value syntax `m _` will no longer be supported in Scala 3.1, since we now have [automatic eta-expansion](https://dotty.epfl.ch/docs/reference/changed-features/eta-expansion-spec.html).

In general you can simply drop the `_` symbol.
Compiling with `dotc -source:3.1-migration -rewrite` rewrites

```scala
def foo(x: Int)(y: Int): Int = x + y
val f = foo _
```

Into

```scala
def foo(x: Int)(y: Int): Int = x + y
val f = foo
```

In the special case of a nullary method, the rewrite rule transforms

```scala
def bar(): Int = 3
val g = bar _
```

Into

```scala
def bar(): Int = 3
val g = (() => bar())
```

### Rule 6 - Add `using` clause to pass explicit arguments to context bound

*This rule breaks the source compatibility with Scala 2.13.*

From Scala 3.1 on, context bounds will map to context parameters.
Thus a `using` clause is needed to pass explicit arguments to them.

Compiling with `dotc -source:3.1-migration -rewrite` rewrites

```scala
def show[T: Show](value: T): String = ???
val intShow = new Show[Int] {}
show(5)(intShow)
```

Into

```scala
def show[T: Show](value: T): String = ???
val intShow = new Show[Int] {}
show(5)(using intShow)
```