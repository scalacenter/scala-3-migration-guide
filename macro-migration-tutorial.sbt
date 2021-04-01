import Versions._

lazy val `macro-cross-lib` = project
  .in(file("macro-migration-tutorial/cross/lib"))
  .settings(
    scalaVersion := scala30Compat,
    crossScalaVersions := Seq(scala213, scala30Compat),
    libraryDependencies ++= {
      if (scalaVersion.value.startsWith("3")) Seq()
      else Seq(
        "org.scala-lang" % "scala-reflect" % scalaVersion.value
      )
    }
  )

lazy val `macro-cross-app` = project
  .in(file("macro-migration-tutorial/cross/app"))
  .settings(
    scalaVersion := scala30Compat,
    crossScalaVersions := Seq(scala213, scala30Compat)
  )
  .dependsOn(`macro-cross-lib`)


lazy val `macro-mix-lib` = project
  .in(file("macro-migration-tutorial/mix/lib"))
  .settings(
    scalaVersion := scala213,
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value
    )
  )

lazy val `macro-mix-macro-lib` = project
  .in(file("macro-migration-tutorial/mix/macro-lib"))
  .settings(
    scalaVersion := scala30Compat
  )
  .dependsOn(`macro-mix-lib`)

lazy val `macro-mix-app` = project
  .in(file("macro-migration-tutorial/mix/app"))
  .settings(
    scalaVersion := scala30Compat,
    crossScalaVersions := Seq(scala213, scala30Compat),
    scalacOptions ++= {
      if (scalaVersion.value.startsWith("3")) Seq()
      else Seq("-Ytasty-reader")
    }
  )
  .dependsOn(`macro-mix-macro-lib`)
