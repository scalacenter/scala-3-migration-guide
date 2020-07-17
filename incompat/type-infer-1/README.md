This incompatibility is inspired by [endpoints4s](https://github.com/endpoints4s/endpoints4s) from this [original commit](https://github.com/endpoints4s/endpoints4s/commit/2b57a0be3978c92eb89c6ab766eb93b5789c171f#diff-d6fdde1c26979b1076788de7854e2ddc).

As of `0.25.0-RC2` the error message is:

```
[error] -- [E007] Type Mismatch Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/src/main/scala/type-infer.scala:13:64 
[error] 13 |  def bar(f: Foo[String] => Int): Option[Int] = Option(foo).map(f)
[error]    |                                                                ^
[error]    |                                      Found:    (f : Foo[String] => Int)
[error]    |                                      Required: Foo[Nothing] => Int
[error] one error found
[error] (Compile / compileIncremental) Compilation failed
```