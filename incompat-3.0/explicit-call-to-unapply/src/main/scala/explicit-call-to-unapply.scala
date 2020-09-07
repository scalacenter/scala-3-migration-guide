case class Location(lat: Int, lon: Int)

object Location {
  def tuple(location: Location): (Int, Int) = {
    locally {
      val Location(lat, lon) = location
      (lat, lon)
    }
  }
}
