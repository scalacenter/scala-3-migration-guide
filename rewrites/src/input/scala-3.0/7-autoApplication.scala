trait Chunk {
  def bytes(): Array[Byte]
  def toSeq: Seq[Byte] = bytes
}
