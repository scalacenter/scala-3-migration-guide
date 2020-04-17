# Scala 3 migration guide

This repository is a continuously evolving guide to support the migration to Scala 3. As a member of the community you are encouraged to participate the effort by sharing your migrating experience.

Since the announcement that Dotty will eventually become Scala 3, the Dotty contributors and the SIP committee is taking great care of the migration accessibility for everyone (library maintainer, application owner, and people teaching others).

A number of complementary initiatives are being undertaken to ease the migration. This repository is not a replacement of those initiatives. Its purpose is to gather knowledge, to collect feedback and to encourage the collaboration. The ultimate goal being to drive the effort of the community toward the release of Scala 3.

All information you may want to know before starting the migration of your codebase should be available in this guide. If not you may want to [contribute](CONTRIBUTING.md).

## Introduction

### What is Scala 3?

Dotty is the project name of the new compiler and improved language that will become Scala 3. 

The compiler has been completely redesigned, which means that the typechecker and type inferencer are totally new. In the corner cases they may not behave as in the scala 2 compiler. The implicit resolution rules have been cleaned up as well.

The language itself has been improved. The changes compared to Scala 2 are limited to:
- some simplified and restricted constructs for safety or consistency reasons
- some dropped features
- and also some exciting new features

Last but not least, the macros system from Scala 2 has been dropped and replaced by a new macro system that is safer, more robust and that breaks the compiler dependency. The downside of it, in terms of migration, is that all the macro usages must be re-written. Chances are that you don't use macros in your codebase, but you depend on libraries that define macro methods.

For a complete overview of the changes in Dotty compared to Scala 2, please visit the [Dotty website](https://dotty.epfl.ch/docs/reference/metaprogramming/toc.html).

### What is left unchanged?

There are reasons to say that Scala 3 is just another Scala version:
- A large subset of the Scala 2 language still compiles to Scala 3.
- The standard library API and implementation are unchanged.
- The ABI (Application Binary Interface) is unchanged, Scala 2 libraries can be used by Scala 3. 

### ABI Compatibility

The bytecode and the IR of scala-js and scala-native, produced by the Scala 2 and Dotty compilers are the same.
It means Scala 2 libraries can be used by Scala 3, as long as the library does not contain macros.
It enables interoperability and gradual migration, and it relieves us from surprising behavior at runtime.

## A Tour of the Migration Tools

### The Dotty compiler

The Dotty compiler itself is a powerful migration tool.

You can use `dotc` on your Scala 2 source code with the `-source 3.0-migration` option. It will do its best to compile most of your code and it will issue a warning for each incompatibility. For a detailed explanation of the `dotc` `-source` option see [#5700](https://github.com/lampepfl/dotty/pull/8700).

Even more than that, in combination with the `-rewrite` flag, the Dotty compiler is able to rewrite some of the Scala 2 code.

Leveraging this tool, the community has already migrated a significant number of well-known libraries, forming the [community build](https://github.com/lampepfl/dotty/tree/master/community-build/community-projects).

The description of these rules are being documented [here](docs/dotty-rewrites.md). If you find any missing rule that is straightforward to implement, please create an issue in the [Dotty repository](https://github.com/lampepfl/dotty).

In case the rule is not trivial to implement you may fall back to the Scalafix tool.

### Scalafix

[Scalafix](https://scalacenter.github.io/scalafix/docs/users/installation.html) may be the complementary tool to assist the compiler for non-trivial incompatibilities.

In particular, it might be very convenient for automatic or semi-automatic resolution of the type inference and implicit resolution incompatibilities, by explicitly writting down the types and implicit values.

Such Scalafix rules could be hosted by the [scala-rewrites](https://github.com/scala/scala-rewrites) repository. [Contributors Welcome!](CONTRIBUTING.md)

### sbt and other build tools

The sbt build tool does already support Dotty and cross-compilation with Scala 2. It is able to glue all the migration tools together to provide the best migration experience.

Other build tools may also support Dotty and cross-compilation with Scala 2. [Contributors Welcome!](CONTRIBUTING.md)

### Metals and other IDEs

Visual Studio Code has its own Dotty Language Server plugin that you can simply configure by running the `sbt launchIDE` task of the `sbt-dotty` plugin.

The Dotty support in Metals is on its way, stay tuned to [#1367](https://github.com/scalameta/metals/issues/1367).

The latest Scala plugin for Intellij Idea already have preliminary support for Scala 3.

[Contributors Welcome!](CONTRIBUTING.md)

### Scaladex

Scala 3 is backward compatible with the Scala 2.13 libraries with the exception of libraries that use macros. If you depend on one of those libraries you may want to check in [Scaladex](https://index.scala-lang.org/), the Scala library index, if this dependency has already been ported to Dotty.

## Content

The `/docs` folder in this repository contains:
- [dotty-rewrites.md](docs/dotty-rewrites.md): A list of the dotty rewrite rules.
- [cross-build.md](docs/cross-build.md): A tutorial for cross building your codebase. This is something that library maintainers would be interested in.
- [upgrade.md](docs/upgrade.md): Applications do not require cross building. You can jump straight and upgrade them to Scala 3 by following this tutorial.
- [macros.md](docs/macros.md): General knowledge about migrating macros.

The structure of this repository is not fully defined yet.

In the near future it may be added:
- A status of the migration related issues that have been submitted to the dotty project or to another migration related project. As well as a list of the on-going initiatives to help the migration.
- A corpus of source code examples that don't cross-compile and how they should translate from scala 2.13 to Dotty.

## Additional Resources

- [The Dotty website](https://dotty.epfl.ch/)
- [The Dotty example projects](https://github.com/lampepfl/dotty-example-project#getting-your-project-to-compile-with-dotty)
- [The Dotty community projects](https://github.com/lampepfl/dotty/tree/master/community-build/community-projects)
- [The Scalafix website](https://scalacenter.github.io/scalafix/)
- [The sbt Cross-Build Manual](https://www.scala-sbt.org/1.x/docs/Cross-Build.html)
