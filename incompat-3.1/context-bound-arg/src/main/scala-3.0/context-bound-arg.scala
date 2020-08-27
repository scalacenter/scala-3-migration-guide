object ContextBoundArg {
  trait Show[T]
  
  def show[T: Show](value: T): String = ???
  
  val intShow = new Show[Int] {}
  
  show(5)(intShow)
}
