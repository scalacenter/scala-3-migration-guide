## Existential Type

Existential type is a [dropped feature](https://dotty.epfl.ch/docs/reference/dropped-features/existential-types.html), which makes the following code illegal.

```scala
def foo: List[Class[T]] forSome { type T }
```

The proposed solution is to introduce an enclosing type that carries a dependent type:

```scala
trait Bar {
  type T
  val value: List[Class[T]]
}

def foo: Bar
```

Note that using a wildcard argument is often simpler but is not always possible.

#### Scala 2 feature warning

Existential type is a Scala 2 feature that must be enabled explicitly by adding the import clause `import scala.language.existentials` or by setting the compiler option `-language:existentials`.

To locate them you must:
  - remove all `import scala.language.existentials``
  - remove the `-language:existentials` compiler option
  - add the `-feature` compiler option
