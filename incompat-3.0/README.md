## Description

In this folder we track all the found incompatibilities between Scala 2 and Scala 3.

Each incompatibility lives in its own subfolder and sbt subproject. It is described by:
- a short `README.md` detailing the origin, the compiler message, the related documentation, the related issues and PRs...
- a `src/main/scala-2.13` source directory that must compile in Scala 2.13 but not in Dotty
- a proposed solution under `src/main/scala/` that must compile with Scala 2.13 and Dotty

The sbt `<incompat>/test` task ensures both that the `scala-2.13` sources does not compile and that the `scala` sources compiles with Dotty. It will help us maintain an up-to-date view of the incompatibilities because, as Dotty progress towards the release of Scala 3, we expect some of these incompatibilites to be solved at the compiler side.

The `implicitView` incompatibility is different because it is a runtime incompatibility, meaning that the code compiles but the runtime behavior change. In this case the `implicitView/test` task checks that the `scala-2.13` runtime behavior is wrong and that the `scala` runtime behavior is correct.

The sbt `incompat30` project is an aggregate of all the incompatibility projects. You can check all the incompatibilities by running `incompat30/test`.

## Motivation

This set of incompatibilities will be used to track the progress of the migration guide and migration tooling.

The [Incompatibility Table](https://scalacenter.github.io/scala-3-migration-guide/docs/incompatibilities/table.html) shows the status of those incomaptibility:
  - Does it produce a feature or deprecation warning in 2.13?
  - Do we have a Dotty migration rewrite?
  - Do we have a Scalafix rule to handle it?
