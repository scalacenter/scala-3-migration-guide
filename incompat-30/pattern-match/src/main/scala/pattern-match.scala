trait Request
case class Fetch[A](ids: Set[A]) extends Request

object Request {
  def combineFetch[A](x: Fetch[_ <: A], y: Fetch[_ <: A]): Fetch[A] = Fetch(x.ids ++ y.ids)

  def combineReq(x: Request, y: Request): Request = {
    (x, y) match {
      case (x @ Fetch(_), y @ Fetch(_)) => combineFetch(x, y)
    }
  }
}
