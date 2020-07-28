package foo

trait A {
  def bar(x: Int): Int = x + 3
}

trait B extends A {
}

class C extends B
