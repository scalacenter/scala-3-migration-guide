# Dotty rewrite rules

## Usage

The Dotty compiler, named `dotc`, has been carefully designed to ease the migration from Scala 2.13 to Scala 3.0. It comes with a handful of utilities to encourage you to cross-compile your codebase:

### Migration mode

The `-source:3.0-migration` option makes the compiler forgiving on most of the dropped features, printing warnings in place of errors. Each warning is a strong indication that the the compiler is capable of safely rewriting the deprecated code into a cross-compiling code.

### Error explanations

The `-source:3.0-migration` mode handles many of the dropped constructs but not all of them. In some cases you will have some remaining errors due to incompatibilities between Scala 2 and Scala 3. The Dotty compiler will not be able to apply the automatic rewrites until you fix those errors. However it can assist you by providing detailed explanations on them. You can enable this by using `-source:3.0-migration` in combination with `-explain`.

This `-explain` option is not limited to migration, it is, in general a wonderful support to learn and code in Scala 3.

### Automatic rewrites

Once your code compiles with the `-source:3.0-migration`, all warnings can be resolved automatically by the addition of the `-rewrite` option. Beware that the compiler will modify your code! It is intended to be safe. However you may like to commit the initial state so that you can print the diff applied by the compiler and revert if necessary.

## Documentation

### Rule 1 - Backquote keywords used as identifiers

Dotty introduces a number of [keywords](https://dotty.epfl.ch/docs/internals/syntax.html#keywords). Some are said _regular_ which means that you cannot use them as identifiers anymore. The others are _soft_ keywords meaning that they are not restricted. You can still use the `open` identifier, for instance.

For the matter of migration from Scala 2 to Scala 3, the keywords at stake are:
- `enum`
- `export`
- `given`
- `then`
- `=>>`
- `?=>`

There is a straightforward solution which is to backquote them. In most cases the rewrite rule can do it for you. Otherwise the compiler will point you toward the solution.

For instance the following Scala 2 code:

```scala
object given {
  val enum = ???

  def foo = println(enum)
}
```

Compiled with `dotc -source:3.0-migration -rewrite` is rewritten into:

```scala
object `given` {
  val `enum` = ???

  def foo = println(`enum`)
}
```

In some cases the compiler is not able to apply the solution by itself but then you can add the `-explain` option and it will point you towards the solution.

With the following line of code:

```scala
def then(): Unit = ()
```

It prints this explanation:

```shell
-- [E040] Syntax Error: src/main/scala/example.scala:2:6 ------------
2 |  def then(): Unit = ()
  |      ^^^^
  |      an identifier expected, but 'then' found

Explanation
===========

If you want to use 'then' as identifier, you may put it in backticks: `then`.
```

### Rule 2 - Add Parentheses around the parameter of a lambda

When followed by its type, the parameter of a lambda is now required to be enclosed in parentheses, making the following Scala 2 code illegal:

```scala
val f = { x: Int => x * x }
```

Compiling with `dotc -source:3.0-migration -rewrite` rewrites it into:

```scala
val f = { (x: Int) => x * x }
```

### Rule 3 - Replace symbol literals by `Symbol` application

Symbol literals are no longer supported, but the `scala.Symbol` class still exists so that each string literal can be safely replaced by an application of `Symbol`.

Although `scala.Symbol` is useful for migration, beware that it has been deprecated in Scala `2.13.3` and it will be removed from `scala-library`. You are recommended, as a second step, to replace them with plain string literals `"xwy"` or a dedicated class.

In the following example:

```scala
val values: Map[Symbol, Int] = Map('abc -> 1)

val abc = values('abc)
```

Compiling with `dotc -source:3.0-migration -rewrite` rewrites the code into:

```scala
val values: Map[Symbol, Int] = Map(Symbol("abc") -> 1)

val abc = values(Symbol("abc"))
```

### Rule 4 - Indent open brace when passing an argument

In Scala 2 it is possible to pass an argument after a new line by enclosing it into braces. Although valid, this style of coding is not encouraged by the [Scala style guide](https://docs.scala-lang.org/style) and it is no longer supported in Scala 3.

Example:
```scala
test("my test")
{
  assert(1 == 1)
}
```

The solution is to indent the block, or at least the first line. Compiling with `dotc -source:3.0-migration -rewrite` rewrites the code into:
```scala
test("my test")
  {
  assert(1 == 1)
}
```

Even though the style of the resulting code is not elegant, it compiles, helping you to make the transition to a different style.

This rule applies to other patterns as well, such as refining a type after a new line. The resulting code looks like:

```scala
type Bar = Foo
  {
  def bar(): Int
}
```

### Rule 5 - Replace `do`-`while` construct with `while` equivalent

The `do` keyword has acquired a different meaning in the [new syntax rules](https://dotty.epfl.ch/docs/reference/other-new-features/control-syntax).

To avoid confusion, the traditional `do <body> while (<cond>)` construct is dropped. It is recommended to use the equivalent `while ({ <body>; <cond> }) ()` that cross-compiles or the new Scala 3 syntax `while { <body>; <cond> } do ()`.

For instance, compiling the following code with `dotc -source:3.0-migration -rewrite` 

```scala
do {
  i += 1
} while (f(i) == 0)
```

rewrites it into

```scala
while ({ {
  i += 1
} ; f(i) == 0}) ()
```

For more information you may refer to [this page](https://dotty.epfl.ch/docs/reference/dropped-features/do-while.html).

### Rule 6 - Drop procedure syntax

Procedure syntax has been deprecated in Scala 2 for a while and it has been dropped in Scala 3, making the following Scala 2 code illegal:

```scala
trait Foo {
  def print()
}

object Bar {
  def print() {
    println("bar")
  }
}
```

Compiling with `dotc -source:3.0-migration -rewrite` rewrites it into:

```scala
trait Foo {
  def print(): Unit 
}

object Bar {
  def print(): Unit = {
    println("bar")
  }
}
```

### Rule 7 - Auto-application

Auto-application is the syntax of calling a nullary method without argument list. In Scala 3, when calling a method defined in Scala 3, auto-application is forbidden.

The following code is now illegal:

```scala
trait Chunk {
  def bytes(): Array[Byte]
  def toSeq: Seq[Byte] = bytes
}
```

Compiling with `dotc -source:3.0-migration -rewrite` rewrites it into:

```scala
trait Chunk {
  def bytes(): Array[Byte]
  def toSeq: Seq[Byte] = bytes()
}
```
Auto-application in Scala 3 is covered in detail in [this page](https://dotty.epfl.ch/docs/reference/dropped-features/auto-apply.html) of the Dotty reference.

## Looking ahead to Scala 3.1

Some deprecations have been postponed to 3.1 to facilitate the migration from 2.13 to 3.0 and then from 3.0 to 3.1. However some of the migration rules are already available and you are likely to be able to apply them in your codebase. In this way you get accustomed to the new syntax and prepared for 3.1.

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