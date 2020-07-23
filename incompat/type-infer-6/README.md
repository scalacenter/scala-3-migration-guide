This incompatibility is inspired by [this commit](https://github.com/scalaz/scalaz/commit/b29aacda99540b638228a7b550bf2f8bc1e8f941) in [scalaz](https://github.com/scalaz/scalaz) 

As of `0.25.0-RC2` the error message is
```
[error] -- Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/type-infer-6/src/main/scala-2.13/type-infer.scala:6:53 
[error] 6 |  def bar[A: Show, B: Show]: Show[A] = foo(implicitly) 
[error]   |                                                     ^
[error]   |ambiguous implicit arguments: both value evidence$2 and value evidence$1 match type T of parameter ev of method implicitly in object DottyPredef
```
