This incompatibility is inspired by [this commit](https://github.com/scalaz/scalaz/commit/59f20aa5b695106b9daeac6c88c0b877d230ddd1) in [scalaz](https://github.com/scalaz/scalaz).

The reason for this incompatibility is described in [this comment](https://github.com/lampepfl/dotty/blob/0f1a23e008148f76fd0a1c2991b991e1dad600e8/compiler/src/dotty/tools/dotc/core/ConstraintHandling.scala#L144-L152).

Unfortunately I don't have a better solution than inlining the implementation or overloading the method with by-name params.

As of `0.25.0-RC2` the error message is:

```
[error] -- [E134] Type Mismatch Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/by-name-param-type-infer/src/main/scala-2.13/by-name-param-type-infer.scala:3:41
[error] 3 |  val g: (Int, => Int) => Int = Function.uncurried(f)
[error]   |                                ^^^^^^^^^^^^^^^^^^
[error]   |None of the overloaded alternatives of method uncurried in object Function with types
[error]   | [T1, T2, T3, T4, T5, R]
[error]   |  (f: T1 => T2 => T3 => T4 => T5 => R): (T1, T2, T3, T4, T5) => R
[error]   | [T1, T2, T3, T4, R](f: T1 => T2 => T3 => T4 => R): (T1, T2, T3, T4) => R
[error]   | [T1, T2, T3, R](f: T1 => T2 => T3 => R): (T1, T2, T3) => R
[error]   | [T1, T2, R](f: T1 => T2 => R): (T1, T2) => R
[error]   |match arguments ((Test.f : Int => (=> Int) => Int))
```
