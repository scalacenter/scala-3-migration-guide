In Scala 3 indentation can cause compilation error.

As of `0.26.0-RC1` the compiler messages are:
```
[error] -- Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/indentation-2/src/main/scala-2.13/indentation.scala:4:12 
[error] 4 |      Vector(1) ++
[error]   |      ^^^^^^^^^
[error]   |postfix operator `++` needs to be enabled
[error]   |by making the implicit value scala.language.postfixOps visible.
[error]   |----
[error]   |This can be achieved by adding the import clause 'import scala.language.postfixOps'
[error]   |or by setting the compiler option -language:postfixOps.
[error]   |See the Scaladoc for value scala.language.postfixOps for a discussion
[error]   |why the feature needs to be explicitly enabled.
```
```
[error] -- [E007] Type Mismatch Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/indentation-2/src/main/scala-2.13/indentation.scala:7:4 
[error] 7 |    foo
[error]   |    ^^^
[error]   |    Found:    (foo : IterableOnce[Int] => Vector[Int])
[error]   |    Required: Vector[Int]
```