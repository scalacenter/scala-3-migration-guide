The implicit `Predef.any2stringadd` conversion is deprecated since `2.13` and dropped in Scala 3.

As of `0.25.0-RC2` the error message is
```
[error] -- [E008] Not Found Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/any2stringadd-conversion/src/main/scala-2.13/any2stringadd-conversion.scala:2:23 
[error] 2 |  val str = new AnyRef + "foo"
[error]   |            ^^^^^^^^^^^^
[error]   |value + is not a member of Object, but could be made available as an extension method.
[error]   |
[error]   |One of the following imports might make progress towards fixing the problem:
[error]   |
[error]   |  import math.Fractional.Implicits.infixFractionalOps
[error]   |  import math.Integral.Implicits.infixIntegralOps
[error]   |  import math.Numeric.Implicits.infixNumericOps
[error]   |
```
