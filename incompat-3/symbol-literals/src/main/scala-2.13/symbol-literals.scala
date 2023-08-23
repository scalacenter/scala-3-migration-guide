object SymbolLitterals {
  val values: Map[Symbol, Int] = Map('abc -> 1)

  val abc = values('abc)
}
