## `+` And `-` As Type Parameters

`+` and `-` are not valid identifiers for type parameters in Scala 3, since they are reserved for variance annotation.

You cannot write `def foo[+]` or `def foo[-]` anymore.

```
-- Error: src/main/scala/type-param-identifier.scala:2:10 
2 |  def foo[+]: +
  |          ^
  |          no `+/-` variance annotation allowed here
```

The solution is to choose another valid identifier, for instance `T`.

However, `+` and `-` still are valid type identifiers in general.
You can write `type +`.