case class Location(lat: Int, lon: Int)

object Location {
  def tuple(location: Location): (Int, Int) = {
    Location.unapply(location).get
  }
}
