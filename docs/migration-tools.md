---
id: migration-tools
title: Tour of the Migration Tools
---

## The Scala 3 compiler

The Scala 3 compiler itself is a powerful migration tool.

It comes with a migration mode that does its best at compiling Scala 2.13 code and it issues migration warnings wherever the code need some care.

Even more than that, it is able to rewrite your code where necessary.

You can learn more about it in the [`Scala 3 Migration Mode`](scala-3-migration-mode.md) section.

Leveraging this tool, the community has already migrated a significant number of well-known libraries, forming the [_Scala 3 Community Build_](https://github.com/lampepfl/dotty/tree/master/community-build/community-projects).

## The Scala 2 compiler

The Scala 2 compiler supports `-Xsource:3`, an option that enables some Scala 3 syntax and behavior.

Most deprecated syntax generates an error.

Infix operators can start a line in the middle of a multiline expression.

Implicit search and overload resolution follow Scala 3 handling of contravariance when checking specificity.

The `-Xsource:3` option is intended to encourage early migration.

## Scalafix

[Scalafix](https://scalacenter.github.io/scalafix/) is a refactoring tool for Scala.
It is the complementary tool to assist the compiler in the migration.

The main advantage of Scalafix is that it can operate in Scala 2.13 so that you can prepare the code before the migration.
It gives you control on the applied changes by proceeding incrementally, one rule at a time.

In particular, it might be very convenient for automatic or semi-automatic resolution of the type inference and implicit resolution problems, by adding some types and implicit values in the code.

## sbt and other build tools

The sbt build tool does already support Scala 3.0 and cross-compilation with Scala 2.13, thanks to the [sbt-dotty plugin](https://dotty.epfl.ch/docs/usage/getting-started.html).

It is able to glue all the migration tools together to provide the best migration experience.

You can learn about cross-building in the [sbt Cross-build Manual](https://www.scala-sbt.org/1.x/docs/Cross-Build.html)

Other build tools also support Scala 3.0 and cross-compilation with Scala 2.13, among which [Mill](http://www.lihaoyi.com/mill/).

[Contributions are welcome!](contributing.md)

## Metals and other IDEs

Visual Studio Code has its own Scala 3.0 Language Server plugin that you can simply configure by running the `sbt launchIDE` task of the `sbt-dotty` plugin.

Scala 3.0 support in Metals, including Scala 3.0 Worksheets, has made significant progress. For more details, check the relevant [Scala Center Update](https://contributors.scala-lang.org/t/metals-and-scala-3/4274).

The latest version of the Scala plugin for Intellij Idea already has preliminary support, including Scala worksheets, for Scala 3.0.

## Scaladex

Check the list of Scala 3.0 open-source libraries in [Scaladex](https://index.scala-lang.org/).
