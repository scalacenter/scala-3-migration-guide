---
id: get-started
title: Introduction
---

Since the announcement that Dotty will become Scala 3, the Scala 3 contributors and the SIP committee are taking great care of the migration accessibility for everyone: library maintainer, application owner, and teachers.

This guide gathers knowledge about the migration. The ultimate goal being to drive the effort of the community toward the release of Scala 3.

All information you may want to know before starting the migration of your codebase should be available in this guide. If you find something's missing, you may want to [contribute](contributing.md).

## Scala 3 (formerly known as Dotty)

The Scala 3 compiler has been written from the ground up and brings a lot of improvements and removes a number of warths
and inconsistencies from the Scala 2 language.

Scala 3 is bringing more consistency, more safety and improved ergonomics by simplifying, restricting and even dropping some of Scala 2 constructs. It also improves the expressiveness of the language by introducing new types and features. 

The compiler has been completely redesigned. In particular the type checker and the type inferencer are different and they do not behave as in Scala 2. The implicit resolution rules have also been reworked.

Last but not least, the macros system from Scala 2 has been dropped and replaced by a new macro system that is safer, more robust and much more stable. It is intended to be forward compatible.

For a complete overview of the changes in Scala 3 compared to Scala 2, please visit the [Scala 3 website](https://dotty.epfl.ch/docs/reference/overview.html).

Despite all those changes, Scala 3 is just another Scala version:
- A large subset of the Scala 2 language still compiles to Scala 3.
- The standard library is unchanged.
- The ABI (Application Binary Interface) is unchanged. Scala 2.13 and Scala 3 libraries can be used on the same classpath.

Surprisingly, the compatibility is better between Scala 2.13 and Scala 3.0 than it was between any two older Scala 2 versions:
- The Scala 3 compiler can read the Scala 2.13 class files, so you can have Scala 2 dependencies in your Scala 3 project.
- The scala 2.13 compiler will soon be able to read the Scala 3 class files as well.

The next section of this guide focuses on the compatibility in the context of the migration. 

## A Tour of the Migration Tools

### The Scala 3 compiler

The Scala 3 compiler itself is a powerful migration tool.

It comes with a migration mode that does its best at compiling Scala 2 code and issues a migration warning wherever the code need some care.

Even more than that, it is able to rewrite your code where necessary.

You can learn more about it in the [`Scala 3 Migration Mode`](dotty-rewrites.md) section.

Leveraging this tool, the community has already migrated a significant number of well-known libraries, forming the [_Scala 3 Community Build_](https://github.com/lampepfl/dotty/tree/master/community-build/community-projects).

### Scalafix

[Scalafix](https://scalacenter.github.io/scalafix/) is a refactoring tool for Scala.
It is the complementary tool to assist the compiler in the migration.

The main advantage of Scalafix is that it can operate in Scala 2.13 so that you can prepare the code before the migration.
It gives you control on the applied changes by proceeding incrementally, one rule at a time.

In particular, it might be very convenient for automatic or semi-automatic resolution of the type inference and implicit resolution problems, by adding some types and implicit values in the code.

### sbt and other build tools

The sbt build tool does already support Scala 3 and cross-compilation with Scala 2, thanks to the [sbt-dotty plugin](https://dotty.epfl.ch/docs/usage/getting-started.html).

It is able to glue all the migration tools together to provide the best migration experience.

Other build tools may also support Scala 3 and cross-compilation with Scala 2. [Contributions are welcome here!](contributing.md)

### Metals and other IDEs

Visual Studio Code has its own Scala 3 Language Server plugin that you can simply configure by running the `sbt launchIDE` task of the `sbt-dotty` plugin.

Scala 3 support in Metals, including Scala 3 Worksheets, has made significant progress. For more detials, check the relevant [Scala Center Update](https://contributors.scala-lang.org/t/metals-and-scala-3/4274).

The latest version of the Scala plugin for Intellij Idea already has preliminary support, including Scala worksheets, for Scala 3.

### Scaladex

Check the list of Scala 3 open-source libraries in [Scaladex](https://index.scala-lang.org/).

## Additional Resources

- [The Scala 3 website](https://dotty.epfl.ch/)
- [The Scala 3 example projects](https://github.com/lampepfl/dotty-example-project#getting-your-project-to-compile-with-dotty)
- [The Scala 3 community projects](https://github.com/lampepfl/dotty/tree/master/community-build/community-projects)
- [The Scalafix website](https://scalacenter.github.io/scalafix/)
- [The sbt Cross-Build Manual](https://www.scala-sbt.org/1.x/docs/Cross-Build.html)
