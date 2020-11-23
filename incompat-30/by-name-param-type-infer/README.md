## `ExprType` as value type

This incompatibility is inspired by [this commit](https://github.com/scalaz/scalaz/commit/59f20aa5b695106b9daeac6c88c0b877d230ddd1) in [scalaz](https://github.com/scalaz/scalaz).

An `ExprType` is a type of the form `=> T` that is used for defining by-name parameters.
Scala 3 does not allow to use them as type parameters.
This decision is explained in [this comment](https://github.com/lampepfl/dotty/blob/0f1a23e008148f76fd0a1c2991b991e1dad600e8/compiler/src/dotty/tools/dotc/core/ConstraintHandling.scala#L144-L152) of the Dotty source code.

For instance, it is now invalid to pass a function of type `Int => (=> Int) => Int` to the `uncurried` method since it would assign `=> Int` to the type parameter `T2`. 

```
-- [E134] Type Mismatch Error: src/main/scala/by-name-param-type-infer.scala:3:41
3 |  val g: (Int, => Int) => Int = Function.uncurried(f)
  |                                ^^^^^^^^^^^^^^^^^^
  |None of the overloaded alternatives of method uncurried in object Function with types
  | [T1, T2, T3, T4, T5, R]
  |  (f: T1 => T2 => T3 => T4 => T5 => R): (T1, T2, T3, T4, T5) => R
  | [T1, T2, T3, T4, R](f: T1 => T2 => T3 => T4 => R): (T1, T2, T3, T4) => R
  | [T1, T2, T3, R](f: T1 => T2 => T3 => R): (T1, T2, T3) => R
  | [T1, T2, R](f: T1 => T2 => R): (T1, T2) => R
  |match arguments ((Test.f : Int => (=> Int) => Int))
```

The solution depends on the situation. In the given example, you can either:
  - define your own `uncurried` method with the appropriate signature
  - inline the implementation of `uncurried` locally
