import Versions._

lazy val website = project
  .in(file("mdoc"))
  .settings(
    publish / skip := true,
    mdoc := (Compile / run).evaluated,
    mdocVariables := Map(
      "scala3" -> scala3,
      "scala3Binary" -> scala3,
      "scala213" -> scala213,
      "sbtDotty" -> sbtDotty,
      "scala3Migrate" -> scala3Migrate,
      "catsCore" -> "2.3.0-M2",
      "osLib" -> osLib,
      "sourcecode" -> sourcecode,
      "munit" -> munit
    )
  )
  .enablePlugins(DocusaurusPlugin)
