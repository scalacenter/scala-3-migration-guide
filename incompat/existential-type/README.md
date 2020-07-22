Existential type is a [dropped feature](https://dotty.epfl.ch/docs/reference/dropped-features/existential-types.html).

The proposed solution is to use a dependent type. Note that using a wildcard is often simpler but it is not always possible. 

As of `0.25.0-RC2` the error message is:
``` 
[error] -- [E034] Syntax Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/existential-type/src/main/scala-2.13/existential-type.scala:2:26 
[error] 2 |  def foo: List[Class[T]] forSome { type T }
[error]   |                          ^^^^^^^
[error]   |                          Existential types are no longer supported -
[error]   |                          use a wildcard or dependent type instead
```