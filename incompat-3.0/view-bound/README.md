View bounds have been deprecated for a long time but they still compile in Scala 2.13

As of `0.25.0-RC2` the error messages are:
``` 
[error] -- Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/view-bound/src/main/scala-2.13/view-bound.scala:2:12 
[error] 2 |  def foo[A <% Long](a: A): Long = a
[error]   |            ^
[error]   |          view bounds `<%' are deprecated, use a context bound `:' instead
```

```
[error] -- [E007] Type Mismatch Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/view-bound/src/main/scala-2.13/view-bound.scala:2:35 
[error] 2 |  def foo[A <% Long](a: A): Long = a
[error]   |                                   ^
[error]   |                                   Found:    (a : A)
[error]   |                                   Required: Long
```
