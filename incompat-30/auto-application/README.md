## Auto-application

Auto-application is the syntax of calling a nullary method without passing an empty argument list.

In Scala 3.0, when calling a method defined in Scala 3.0, this syntax is forbidden.

The following code is now illegal:

```scala
trait Chunk {
  def bytes(): Seq[Byte]
  def toSeq: Seq[Byte] = bytes
}
```

#### Scala 2.13 deprecation

This syntax is already deprecated in Scala 2.13.
It produces a warning of the following form:

```
src/main/scala/auto-application.scala:3:26: Auto-application to `()` is deprecated. Supply the empty argument list `()` explicitly to invoke method bytes,
or remove the empty argument list from its definition (Java-defined methods are exempt).
In Scala 3, an unapplied method like this will be eta-expanded into a function.
  def toSeq: Seq[Byte] = bytes
```

#### Scala 3 migration rewrite

Compiling with Scala 3.0 and the `-source:3.0-migration -rewrite` compilation options rewrites it into:

```scala
trait Chunk {
  def bytes(): Seq[Byte]
  def toSeq: Seq[Byte] = bytes()
}
```

Auto-application is covered in detail in [this page](https://dotty.epfl.ch/docs/reference/dropped-features/auto-apply.html) of the Scala 3.0 reference documentation.

#### Scalafix rule

Alternatively you can use the [`scala/scala-rewrites`](https://index.scala-lang.org/scala/scala-rewrites/scala-rewrites/0.1.2?target=_2.13) rule named `fix.scala213.ExplicitNonNullaryApply`.
