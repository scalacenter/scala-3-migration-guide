trait Chunk {
  def bytes(): Seq[Byte]
  def toSeq: Seq[Byte] = bytes()
}