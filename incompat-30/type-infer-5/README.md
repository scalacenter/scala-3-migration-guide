This incompatibility is inspired by [this commit](https://github.com/scalaz/scalaz/commit/bf4710dca45842b0833d83806353dc21b4ee2c2c) in [scalaz](https://github.com/scalaz/scalaz).

As of `0.25.0-RC2` the error message is:
``` 
[error] -- [E007] Type Mismatch Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/type-infer-5/src/main/scala-2.13/type-infer.scala:6:4 
[error] 6 |    result
[error]   |    ^^^^^^
[error]   |    Found:    (result : Array[Nothing])
[error]   |    Required: Array[A]
```
