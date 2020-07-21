trait Bar {
  val name: String
  val size: Int = name.size
}

abstract class BarEarlyInit(val name: String) extends Bar

object Foo extends BarEarlyInit("Foo")
