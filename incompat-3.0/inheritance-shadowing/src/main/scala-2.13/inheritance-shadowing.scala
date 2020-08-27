object InheritShadowing {
  class A {
    val x = 2
  }

  object B {
    val x = 1
    class C extends A {
      println(x)
    }
  }
}
