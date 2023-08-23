import reflect.ClassTag

object Test {
  def newArray[A: ClassTag]: Array[A] = {
    val result = new Array(5)
    result
  }
}
