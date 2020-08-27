This incompatibility is inspired by [endpoints4s](https://github.com/endpoints4s/endpoints4s) from this [original commit](https://github.com/endpoints4s/endpoints4s/commit/2b57a0be3978c92eb89c6ab766eb93b5789c171f#diff-5c9041420152c9d38de8ccdac0d03034).

As of `0.25.0-RC2` the error message is:

```
[error] -- [E007] Type Mismatch Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/type-infer-2/src/main/scala-2.13/type-infer.scala:6:62 
[error] 6 |  def bar(implicit ctx: Context): (Option[ctx.Out], String) = (foo, "foo")
[error]   |                                                              ^^^^^^^^^^^^
[error]   |                                       Found:    (Option[Any], String)
[error]   |                                       Required: (Option[ctx.Out], String)
```
