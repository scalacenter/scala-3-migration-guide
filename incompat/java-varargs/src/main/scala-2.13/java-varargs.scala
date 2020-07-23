import scala.annotation.varargs

object Foo {
  @varargs def foo(values: String*): Unit = ???
}
