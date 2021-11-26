addSbtPlugin("org.scalameta" % "sbt-mdoc" % "2.2.23")
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.33")

libraryDependencies ++= Seq(
  "io.github.java-diff-utils" % "java-diff-utils" % "4.11"
)
