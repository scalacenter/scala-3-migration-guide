This incompatibility is inspired by the [dotty-staging fork](https://github.com/dotty-staging/fastparse/tree/e6e15f43003cbefc93bcd1209c37d8a4ed4d2f64) of [fastparse](https://github.com/lihaoyi/fastparse) from this [original commit](https://github.com/dotty-staging/fastparse/commit/5bb27834d58422282bc12a1b5a03a69dc081338e).

## Error message

As of `0.25.0-RC2` the error message is:

```
[error] 4 |  def foo[_: Foo]: Unit = ()
[error]   |          ^
[error]   |          an identifier expected, but '_' found
```

## Explanation

The usage of `_` as a type parameter in Scala 2 is not official. It is used, in fastparse and elsewhere, associated with a context bound to declare an implicit parameter. Odersky has described this pattern as a "clever exploit of a scalac compiler bug" ([source](https://www.reddit.com/r/scala/comments/fczcvo/mysterious_context_bounds_in_fastparse_2/fjecokn/)).

This pattern is incompatible with Scala 3 because the new compiler expect a valid identifier instead of `_`. In the proposed solution we have chosen `$` but any other identifier would do the trick as well.

