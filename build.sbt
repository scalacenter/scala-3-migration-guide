import Versions._

lazy val website = project
  .in(file("mdoc"))
  .settings(
    skip.in(publish) := true,
    mdoc := run.in(Compile).evaluated,
    mdocVariables := Map(
      "scala30" -> scala30,
      "scala30Binary" -> scala30,
      "scala213" -> scala213,
      "sbtDotty" -> sbtDotty,
      "catsCore" -> "2.3.0-M2",
      "osLib" -> osLib,
      "sourcecode" -> sourcecode
    )
  )
  .enablePlugins(DocusaurusPlugin)
