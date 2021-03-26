import Versions._

lazy val website = project
  .in(file("mdoc"))
  .settings(
    publish / skip := true,
    mdoc := (Compile / run).evaluated,
    mdocVariables := Map(
      "scala30" -> scala30,
      "scala30Binary" -> scala30,
      "scala213" -> scala213,
      "sbtDotty" -> sbtDotty,
      "scala3Migrate" -> scala3Migrate,
      "catsCore" -> "2.3.0-M2",
      "osLib" -> osLib,
      "sourcecode" -> sourcecode
    )
  )
  .enablePlugins(DocusaurusPlugin)
