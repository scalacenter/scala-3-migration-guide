## `_` As A Type Parameter

This incompatibility is inspired by [this commit](https://github.com/dotty-staging/fastparse/commit/5bb27834d58422282bc12a1b5a03a69dc081338e#diff-44bef84b6823ecb2fd75c0c8b5749bd2) in the [dotty-staging fork](https://github.com/dotty-staging/fastparse/tree/e6e15f43003cbefc93bcd1209c37d8a4ed4d2f64) of [lihaoyi/fastparse](https://github.com/lihaoyi/fastparse) project.

The usage of the `_` identifier as a type parameter is permitted by the Scala 2.13, even if it has never been mentioned in the Scala 2 specification.
It is used in the fastparse API, in combination with a context bound, to declare an implicit parameter.

```sala
def foo[_: Foo]: Unit = ???
```

Here, the method `foo` takes a type paramater `_` and an implicit parameter of type `Foo[_]` where `_` refers to the type parameter, not the wildcard symbol.  

Martin Odersky described this pattern as a "clever exploit of a scalac compiler bug" ([source](https://www.reddit.com/r/scala/comments/fczcvo/mysterious_context_bounds_in_fastparse_2/fjecokn/)).

The Scala 3.0 compiler does not permit this pattern anymore: 

```
-- [E040] Syntax Error: src/main/scala/anonymous-type-param.scala:4:10
4 |  def foo[_: Foo]: Unit = ()
  |          ^
  |          an identifier expected, but '_' found
```

The solution is to give the parameter a valid identifier name, for instance `T`.
