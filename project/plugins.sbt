addSbtPlugin("org.scalameta" % "sbt-mdoc" % "2.2.18")
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.27")

libraryDependencies ++= Seq(
  "io.github.java-diff-utils" % "java-diff-utils" % "4.9"
)
