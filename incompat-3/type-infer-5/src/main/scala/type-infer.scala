import reflect.ClassTag

object Test {
  def newArray[A: ClassTag]: Array[A] = {
    val result = new Array[A](5)
    result
  }
}
