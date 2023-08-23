This incompatibility is inspired by [this commit](https://github.com/scalaz/scalaz/commit/45ec3197db959794b0c4642889c8fb1f133a8b3b) in [scalaz](https://github.com/scalaz/scalaz).

As of `0.25.0-RC2` the error message is:

``` 
[error] -- [E007] Type Mismatch Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/type-infer-4/src/main/scala-2.13/type-infer.scala:9:62 
[error] 9 |  def buzz[A, B](foo: Foo[A]): Option[Bar[B]] = fizz(foo).map(toBar)
[error]   |                                                              ^^^^^
[error]   |                                                        Found:    Bar[Any]
[error]   |                                                        Required: Bar[B]
```
