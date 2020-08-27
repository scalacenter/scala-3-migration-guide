Suprisingly if I replace `IO.succeed _` with `IO.succeed(_)` it compiles successfully.

As of `0.25.0-RC2` the error message is
``` 
[error] -- [E007] Type Mismatch Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/type-infer-8/src/main/scala-2.13/type-infer.scala:4:76 
[error] 4 |  def collect[B](pf: PartialFunction[A, B]): IO[B, E] = collectM(pf.andThen(IO.succeed _))
[error]   |                                                                            ^^^^^^^^^^
[error]   |                                         Found:    Any => IO[Any, Nothing]
[error]   |                                         Required: B => IO[B, E]
```
