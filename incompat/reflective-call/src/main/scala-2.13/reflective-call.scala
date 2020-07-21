import scala.language.reflectiveCalls

object Foo {
  val foo = new {
    def bar: Unit = ???
  }

  foo.bar
}
