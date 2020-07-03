object IndentArgument {
  def test(title: String)(body: => Unit) = ???
  
  test("my test")
    {
    assert(1 == 1)
  }
}
