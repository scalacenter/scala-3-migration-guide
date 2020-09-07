## Inheritance Shadowing

An inherited member, from a parent trait or class, can shadow an identifier defined in a outer scope.
That pattern is called inheritance shadowing.

```scala
object B {
  val x = 1
  class C extends A {
    println(x)
  }
}
```

For instance, in this preceding piece of code, the `x` term in C can refer to the `x` member defined in the outer class `B` or it can refer to a `x` member of the parent class `A`.
You cannot know until you go to the definition of `A`.

This is known for being error prone.

In Scala 3, if the parent class `A` does actually have a member `x`, the compiler will require disambiguation.

It prevents the following piece of code from compiling.

```scala
class A {
  val x = 2
}

object B {
  val x = 1
  class C extends A {
    println(x)
  }
}
```

```
-- [E049] Reference Error: src/main/scala/inheritance-shadowing.scala:9:14 
9 |      println(x)
  |              ^
  |              Reference to x is ambiguous,
  |              it is both defined in object B
  |              and inherited subsequently in class C
```

To avoid any ambiguity you have to write `this.x` instead of `x`.

#### Dotty migration rule

The Dotty compiler can automatically disambiguate the code when called with the `-source:3.0-migration -rewrite` options.

In this case it replaces `println(x)` by `println(this.x)`.
