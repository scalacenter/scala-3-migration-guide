import Version._

Global / resolvers += "scala-integration".at("https://scala-ci.typesafe.com/artifactory/scala-integration/")

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


lazy val mixLib = project
  .in(file("macro/mix/lib"))
  .settings(
    scalaVersion := scala213,
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value
    )
  )

lazy val mixMacroLib = project
  .in(file("macro/mix/macro-lib"))
  .settings(
    scalaVersion := dotty
  )
  .dependsOn(mixLib)

lazy val mixApp = project
  .in(file("macro/mix/app"))
  .settings(
    scalaVersion := dotty,
    crossScalaVersions := Seq(`scala-2.13.4`, dotty)
  )
  .dependsOn(mixMacroLib)
