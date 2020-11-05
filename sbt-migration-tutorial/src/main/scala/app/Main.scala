package app

import cats.data._
import sourcecode._
import os.Path

object Main extends App {
  def debug[V](value: Text[V])(implicit file: File) = {
    assert(os.exists(Path(file.value)))
    println(s"${value.source}: ${value.value}")
  }
  
  debug(Chain.fromSeq(List(1, 2)))
}
