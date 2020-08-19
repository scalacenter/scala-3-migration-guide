// TODO Uncomment on Dotty 0.26.0 or Dotty 0.27.0-RC1

// object PatternBinding {
//   val list: List[Int] = List(1)
//   val head :: _ = list

//   val listOpt: List[Option[Int]] = List(Some(1), None)
//   for (Some(value) <- listOpt)
//     println(value)
// }
