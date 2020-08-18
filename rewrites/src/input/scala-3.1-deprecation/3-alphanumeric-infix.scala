object AlphanumericInfix {
  trait MultiSet {
    def difference(other: MultiSet): MultiSet
  }

  def test(s1: MultiSet, s2: MultiSet): MultiSet = 
    s1 difference s2
}
