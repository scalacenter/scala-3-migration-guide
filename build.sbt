import org.apache.commons.io.FileUtils

val dottyVersion = "0.25.0-RC2"

val inputDir = settingKey[File]("Directory containing the source files that will be rewitten")
val outputDir = settingKey[File]("Directory in which the source files are rewritten")
val checkDir = settingKey[File]("Directory that contains the expected rewritten files")
val migration = settingKey[String]("Target Scala version - 3.0 or 3.1")

val rewrites = (project in file("rewrites"))
  .settings(
    scalaVersion := dottyVersion,
    migration := "3.0",
    scalacOptions ++= Seq(s"-source:${migration.value}-migration","-rewrite"),
    inputDir := baseDirectory.value / s"src/input/scala-${migration.value}",
    outputDir := target.value / s"src-managed/main/scala-${migration.value}",
    checkDir := baseDirectory.value / s"src/check/scala-${migration.value}",
    Compile / sourceGenerators += Def.task { copySources(inputDir.value, outputDir.value) },
    Test / test := Def.task {
      new FileChecker(outputDir.value, checkDir.value, state.value.log).run() 
    }.dependsOn(Compile / compile).value
  )

def copySources(inputDir: File, outputDir: File): Seq[File] = {
  if (outputDir.exists) FileUtils.deleteDirectory(outputDir)
  FileUtils.copyDirectory(inputDir, outputDir)
  outputDir.listFiles.filter(_.isFile)
}
