addSbtPlugin("ch.epfl.lamp" % "sbt-dotty" % "0.4.1")

ThisBuild / libraryDependencies ++= Seq(
  "org.apache.commons" % "commons-io" % "1.3.2",
  "io.github.java-diff-utils" % "java-diff-utils" % "4.5"
)
