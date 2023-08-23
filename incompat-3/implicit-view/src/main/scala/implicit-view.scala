trait Pretty {
  val print: String
}

object Pretty {
  implicit def anyPretty(any: Any): Pretty = new Pretty { val print = "any" }
  def pretty[A](a: A)(implicit ev: A => Pretty): String = ev(a).print
}

object Test extends App {
  assert(Pretty.pretty("foo")(str => new Pretty { val print = str }) == "foo")
}
