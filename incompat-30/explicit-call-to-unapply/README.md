## Explicit Call To `unapply`

In Scala, case classes have an auto-generated extractor method, called `unapply` in their companion object.
Its signature has changed between Scala 2.13 and Scala 3.

The new signature is option-less (see the new [Pattern Matching reference](https://dotty.epfl.ch/docs/reference/changed-features/pattern-matching.html)),which causes an incompatibility when `unapply` is called explicitly.

Note that this problem does not affect user-defined extractors, whose signature stays the same across Scala versions.

Given the following case class definition:

``` scala
case class Location(lat: Double, long: Double)
```

The Scala 2.13 compiler generates the following `unapply` method:

``` scala
object Location {
  def unapply(location: Location): Option[(Double, Double)] = Some((location.lat, location.long))
}
```

Whereas the Scala 3 compiler generates:

``` scala
object Location {
  def unapply(location: Location): Location = location
}
```

Consequently the following code does not compile anymore.

``` scala
def tuple(location: Location): (Int, Int) = {
  Location.unapply(location).get
}
```

A possible solution is to use pattern binding:

``` scala
def tuple(location: Location): (Int, Int) = {
  val Location(lat, lon) = location
  (lat, lon)
}
```
