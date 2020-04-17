# Macros migration

The Scala 2 macros are compiler dependent. In other words, macros defined in a Scala 2 library cannot be consumed by a different compiler version.

Scala 3 will break this limitation but it comes with the cost of rewriting all the macro usages of the Scala ecosystem.

## Migration status

While being experimental, the Scala community has largely adopted the Scala 2 [Def Macro](https://docs.scala-lang.org/overviews/macros/overview.html) feature in multiple of ways: code generation, optimizations, ergonomic DSLs...

Here is an incomplete list of libraries that use Scala 2 macros and their migration status:
[Contributors welcome!](CONTRIBUTING.md)

## How to?

[Contributors welcome!](CONTRIBUTING.md)

## Additional Resources

- [Dotty Documentation](https://dotty.epfl.ch/docs/reference/metaprogramming/toc.html)
- [Macros: The Plan For Scala 3](https://www.scala-lang.org/blog/2018/04/30/in-a-nutshell.html)