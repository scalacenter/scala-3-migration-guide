This incompatibility is inspired by [this commit](https://github.com/zio/zio/commit/47354b798aaf1969d2980feda1d7bbe829c45429#diff-36069d491b953beec25f10121bf83d38) from [zio](https://github.com/zio/zio)
 
The original incompatible was later solved, in this [commit](https://github.com/zio/zio/commit/9452227590657a992d66f77d348be6b4be5055ff), by precising the variance of the related types, making the type annotation useless. However I was not able to reproduce this alternative fix in the present incompatibility.

As of `0.25.0-RC2` the error message is
```
[error] -- [E007] Type Mismatch Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/type-infer-9/src/main/scala-2.13/type-infer.scala:16:58 
[error] 16 |  def foo(io: IO[Nothing]): IO[Nothing] = acquire(cleanup)(io)
[error]    |                                          ^^^^^^^^^^^^^^^^^^^^
[error]    |                                          Found:    IO[Any]
[error]    |                                          Required: IO[Nothing]
```
