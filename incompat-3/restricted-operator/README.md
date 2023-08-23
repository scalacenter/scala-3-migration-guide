This incompatibility is inspired by [this commit](https://github.com/scalaz/scalaz/commit/56b5cbb39ba82ae1a3641bbadda419b17171a219) in [scalaz](https://github.com/scalaz/scalaz) 

`=>>` is not valid identifier anymore and the dotc rewrite feature does not handle this case.

As of `0.25.0-RC2` the error message is
```
[error] -- [E040] Syntax Error: /src/main/scala/restricted-operator.scala:2:6 
[error] 2 |  def =>> : Unit
[error]   |      ^^^
[error]   |      an identifier expected, but '=>>' found
```
