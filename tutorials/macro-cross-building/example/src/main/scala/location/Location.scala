package location
case class Location(path: String, line: Int) {
  override def toString(): String = s"Line $line in $path"
}
