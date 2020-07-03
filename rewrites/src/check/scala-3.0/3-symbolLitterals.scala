object SymbolLitterals {
  val values: Map[Symbol, Int] = Map(Symbol("abc") -> 1)

  val abc = values(Symbol("abc"))
}
