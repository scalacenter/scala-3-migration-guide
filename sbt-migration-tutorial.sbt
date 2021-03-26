import Versions._

lazy val `sbt-migration-tutorial` = project
  .in(file("sbt-migration-tutorial"))
  .settings(
    scalaVersion := scala30,
    crossScalaVersions := Seq(scala213, scala30),
    libraryDependencies ++= Seq(
      "org.typelevel" % "cats-core_2.13" % "2.1.1",
      "com.lihaoyi" %% "os-lib" % osLib, // TODO use "os-lib_3.0"
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
