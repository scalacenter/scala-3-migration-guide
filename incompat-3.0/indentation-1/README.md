In Scala 3 indentation can cause compilation error.

As of `0.26.0-RC1` the compiler messages are:
```
[error] -- [E050] Type Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/indentation-1/src/main/scala-2.13/indentation.scala:4:14 
[error] 4 |    val bar = foo
[error]   |              ^^^
[error]   |              value foo does not take parameters
```
```
[error] -- [E007] Type Mismatch Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/indentation-1/src/main/scala-2.13/indentation.scala:6:3 
[error] 6 |  }
[error]   |   ^
[error]   |   Found:    Unit
[error]   |   Required: (Int, Int)
```