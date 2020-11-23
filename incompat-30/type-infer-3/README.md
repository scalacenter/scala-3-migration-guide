This incompatibility is inspired by the [dotty-staging-fork](https://github.com/dotty-staging/geny/tree/eabca8efc9b76516003f686b3c20f798b5ac011e) of [geny](https://github.com/lihaoyi/geny) from this [original commit](https://github.com/dotty-staging/geny/commit/d62edce1ec69c6ecd2061da6b4bf22bd4dba6230#diff-d2df9cc4eedc3f846d7431c70df2ff81).

As of `0.25.0-RC2` the error message is:

``` 
[error] -- Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/type-infer-3/src/main/scala-2.13/type-infer.scala:14:12 
[error] 14 |  from(Fizz)
[error]    |            ^
[error]    |no implicit argument of type Context[Bar] was found for parameter ctx of method from in object Test
```