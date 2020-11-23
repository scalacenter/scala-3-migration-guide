lazy val website = project
  .in(file("mdoc"))
  .settings(
    skip.in(publish) := true,
    mdoc := run.in(Compile).evaluated,
  )
  .enablePlugins(DocusaurusPlugin)
