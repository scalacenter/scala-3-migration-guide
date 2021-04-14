---
id: runtime
title: Runtime
---

Scala 2.13 and Scala 3 share the same Application Binary Interface (ABI).

> The ABI  is the representation of Scala code in bytecode or Scala.js IR.
> It determines the run-time behavior of a piece of code.

Compiling the same piece of code in Scala 2.13 and Scala 3 produces very similar pieces of bytecode.
The difference being that some features have changed, for instance the initialization of lazy vals has been improved. 

Sharing the ABI also ensures that Scala 2.13 and Scala 3 class files can be loaded by the same JVM class loader.
Similarly, that Scala 2.13 and Scala 3 `sjsir` files can be linked together by the Scala.js linker.

Furthermore it relieves us from a lot of surprising behaviors at runtime.
It makes the migration from Scala 2.13 to Scala 3 very safe in terms of run-time crashes and performance.

At first sight the runtime behavior of a Scala program is not better nor worse in Scala 3 compare to Scala 2.13.
However some new features will help you optimize your program:
- [Opaque Type Aliases](http://dotty.epfl.ch/docs/reference/other-new-features/opaques.html)
- [Inline Methods](http://dotty.epfl.ch/docs/reference/metaprogramming/inline.html)
- [@threadUnsafe annotation](http://dotty.epfl.ch/docs/reference/other-new-features/threadUnsafe-annotation.html)
