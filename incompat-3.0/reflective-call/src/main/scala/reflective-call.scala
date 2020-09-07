import scala.language.reflectiveCalls

object Foo {
  val foo: { def bar: Unit } = new {
    def bar: Unit = ???
  }

  foo.bar
}
