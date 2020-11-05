import Version._

lazy val `sbt-migration-tutorial` = project
  .in(file("sbt-migration-tutorial"))
  .settings(
    semanticdbEnabled := true,
    scalaVersion := dotty,
    crossScalaVersions := Seq(scala213, dotty),
    libraryDependencies ++= Seq(
      "org.typelevel" % "cats-core_2.13" % "2.2.0",
      "com.lihaoyi" % "os-lib_0.27" % "0.7.1",
      "com.lihaoyi" %% "sourcecode" % "0.2.1"
    )
  )