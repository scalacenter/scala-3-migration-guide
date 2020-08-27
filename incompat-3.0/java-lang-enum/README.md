This incompatibility is inspired by [this commit](https://github.com/ekrich/sconfig/commit/76f2ea0b3d4beb20887a0ee63e9f151303843f9e#diff-972416eb7520889dc7c1749326914527) in [sconfig](https://github.com/ekrich/sconfig) 

As of `0.25.0-RC2` the error message is
``` 
[error] -- [E053] Type Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/java-lang-enum/src/main/scala-2.13/java-lang-enum.scala:2:15 
[error] 2 |  def foo[E <: Enum[E]](e: Enum[E]): Unit
[error]   |               ^^^^^^^
[error]   |               Enum does not take type parameters
```

```
[error] -- [E053] Type Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/java-lang-enum/src/main/scala-2.13/java-lang-enum.scala:2:27 
[error] 2 |  def foo[E <: Enum[E]](e: Enum[E]): Unit
[error]   |                           ^^^^^^^
[error]   |                           Enum does not take type parameters
```
