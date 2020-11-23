
## `java.lang.Enum`

This incompatibility is inspired by [this commit](https://github.com/ekrich/sconfig/commit/76f2ea0b3d4beb20887a0ee63e9f151303843f9e#diff-972416eb7520889dc7c1749326914527) in [sconfig](https://github.com/ekrich/sconfig).

The new [`Enum`](https://dotty.epfl.ch/api/scala/Enum.html) trait in the Scala 3 library shadows the `java.lang.Enum` class, causing the following compilation errors:

``` 
-- [E053] Type Error: src/main/scala/java-lang-enum.scala:2:15 
2 |  def foo[E <: Enum[E]](e: Enum[E]): Unit
  |               ^^^^^^^
  |               Enum does not take type parameters
```
 
You can simply fix this issue by using the absolute path of `java.lang.Enum` or by aliasing it.  
