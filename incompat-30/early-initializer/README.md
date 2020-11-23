## Early Initializer

The early initializer feature is dropped, as explained [here](https://dotty.epfl.ch/docs/reference/dropped-features/early-initializers.html), making the following code illegal.

```scala
trait Bar {
  val name: String
  val size: Int = name.size
}

object Foo extends {
  val name = "Foo"
} with Bar
```

The Scala 3 compiler produces the following error messages:

```
-- Error: src/main/scala/early-initializer.scala:6:19 
6 |object Foo extends {
  |                   ^
  |                   `extends` must be followed by at least one parent
```

```
-- [E009] Syntax Error: src/main/scala/early-initializer.scala:8:2 
8 |} with Bar
  |  ^^^^
  |  Early definitions are not supported; use trait parameters instead
```

In Scala 3, trait parameters eliminate the need of early initializers.
But trait parameters do not compile in Scala 2.

The proposed solution, for the time of the migration, is to introduce an intermediate abstract class that carries the early initialized `val`s and `var`s as constructor parameters.

```scala
abstract class BarEarlyInit(val name: String) extends Bar

object Foo extends BarEarlyInit("Foo")
```

In the case of a class, it would be also possible to use a secondary constructor, that would initiate the class by calling the private primary constructor, as shown by this example:

```scala
class Fizz private (val name: String) extends Bar {
  def this() = this("Fizz")
}
```

#### Scala 2 deprecation

The Scala 2 compiler emits a deprecation warning on early initializers:

```
src/main/scala/early-initializer.scala:6:20: early initializers are deprecated; they will be replaced by trait parameters in 3.0, see the migration guide on avoiding var/val in traits.
object Foo extends {
                   ^
```
