import Versions._

lazy val `sbt-migration-tutorial` = project
  .in(file("sbt-migration-tutorial"))
  .settings(
    scalaVersion := scala30Compat,
    crossScalaVersions := Seq(scala213, scala30Compat),
    libraryDependencies ++= Seq(
      "org.typelevel" % "cats-core_2.13" % "2.1.1",
      "com.lihaoyi" %% "os-lib" % osLib,
      "com.lihaoyi" %% "sourcecode" % sourcecode
    ),
    scalacOptions ++= {
      if (scalaVersion.value.startsWith("3")) Seq(
        "-encoding",
        "UTF-8",
        "-feature",
        "-unchecked",
        "-language:implicitConversions",
        // "-Xfatal-warnings",
      ) else Seq(
        "-encoding",
        "UTF-8",
        "-feature",
        "-deprecation",
        "-language:implicitConversions",
        "-Xfatal-warnings",
        "-Wunused:imports,privates,locals",
        "-Wvalue-discard",
        // "-Ytasty-reader"
      )
    },
    scalacOptions ++= { 
      if (scalaVersion.value.startsWith("3")) Seq("-source:3.0-migration")
      else Seq.empty
    }
  )
