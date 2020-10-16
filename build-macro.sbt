import Version._

lazy val crossLib = project
  .in(file("macro/cross/lib"))
  .settings(
    scalaVersion := dotty,
    crossScalaVersions := Seq(scala213, dotty),
    libraryDependencies ++= {
      if (isDotty.value) Seq()
      else Seq(
        "org.scala-lang" % "scala-reflect" % scalaVersion.value
      )
    }
  )

lazy val crossApp = project
  .in(file("macro/cross/app"))
  .settings(
    scalaVersion := dotty,
    crossScalaVersions := Seq(scala213, dotty)
  )
  .dependsOn(crossLib)
