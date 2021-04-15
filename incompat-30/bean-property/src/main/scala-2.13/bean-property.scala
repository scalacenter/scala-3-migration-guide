import scala.beans.BeanProperty

class Pojo {
  @BeanProperty var fooBar: String = ""
}

object Test {
  def test(): Unit = {
    val pojo = new Pojo
    println(pojo.getFooBar())
  }
}
