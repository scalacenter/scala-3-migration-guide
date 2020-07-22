This incompatibility is inspired by [this commit](https://github.com/scalaz/scalaz/commit/856f073da2b781fea8aa2e7ff06213a075519fb6) in [scalaz](https://github.com/scalaz/scalaz) 

`+` and `-` are not valid identifiers for type parameters anymore.

However they still are valid type identifiers in general:
```
type +
```

As of `0.25.0-RC2` the error messages are
```
[error] -- Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/type-param-identifier/src/main/scala-2.13/type-param-identifier.scala:2:10 
[error] 2 |  def foo[+]: +
[error]   |          ^
[error]   |          no `+/-` variance annotation allowed here
```

``` 
[error] -- [E006] Not Found Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/type-param-identifier/src/main/scala-2.13/type-param-identifier.scala:2:14 
[error] 2 |  def foo[+]: +
[error]   |              ^
[error]   |              Not found: type +
```
