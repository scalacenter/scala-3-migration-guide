sealed trait Callback[A]

sealed trait State[+A]
final case class Executing[A](observer: Callback[Option[A]]) extends State[A]

class Test[A](state: State[A]) {
  def notify(observer: Callback[Option[A]]): Unit = ???

  def test(): Unit = {
    state match {
      case Executing(observer) => notify(observer)
    }
  }
}
