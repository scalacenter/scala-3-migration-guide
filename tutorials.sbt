import Versions._

lazy val `sbt-migration-tutorial` = project
  .in(file("tutorials/sbt-migration"))
  .settings(
    scalaVersion := scala30,
    crossScalaVersions := Seq(scala213, scala30),
    libraryDependencies ++= Seq(
      "org.typelevel" % "cats-core_2.13" % "2.1.1",
      "com.lihaoyi" %% "os-lib" % osLib,
      "com.lihaoyi" %% "sourcecode" % sourcecode
    ),
    scalacOptions ++= {
      Seq(
        "-encoding",
        "UTF-8",
        "-feature",
        "-language:implicitConversions",
        // "-Xfatal-warnings" disabled during the migration,
      ) ++ 
        (CrossVersion.partialVersion(scalaVersion.value) match {
          case Some((3, _)) => Seq(
            "-unchecked",
            "-source:3.0-migration"
          )
          case _ => Seq(
            "-deprecation",
            "-Xfatal-warnings",
            "-Wunused:imports,privates,locals",
            "-Wvalue-discard"
          )
        })
    }
  )

lazy val `macro-cross-building-example` = project
  .in(file("tutorials/macro-cross-building/example"))
  .settings(
    scalaVersion := scala30,
    crossScalaVersions := Seq(scala213, scala30),
    libraryDependencies ++= {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 13)) => Seq(
          "org.scala-lang" % "scala-reflect" % scalaVersion.value,
        )
        case _ => Seq.empty
      }
    },
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % Versions.munit % Test
    )
  )

lazy val `macro-mixing-example` = project
  .in(file("tutorials/macro-mixing/example"))
  .settings(
    scalaVersion := scala30
  )
  .dependsOn(`macro-mixing-example-compat`)

lazy val `macro-mixing-example-compat` = project
  .in(file("tutorials/macro-mixing/example-compat"))
  .settings(
    scalaVersion := scala213,
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value
    )
  )

lazy val `macro-mixing-example-test` = project
  .in(file("tutorials/macro-mixing/example-test"))
  .settings(
    scalaVersion := scala30,
    crossScalaVersions := Seq(scala30, scala213),
    scalacOptions ++= {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 13)) => Seq("-Ytasty-reader")
        case _ => Seq.empty
      }
    },
    libraryDependencies += "org.scalameta" %% "munit" % munit % Test
  )
  .dependsOn(`macro-mixing-example`)
