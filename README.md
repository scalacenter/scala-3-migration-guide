# Scala 3 migration guide

This repository is a continuously evolving guide to support the migration to Scala 3. As a member of the community you are encouraged to participate the effort by sharing your migrating experience.
Visit https://scalacenter.github.io/scala-3-migration-guide to learn more about the migration guide.

A number of complementary initiatives are being undertaken to ease the migration. This repository is not a replacement of those initiatives. Its purpose is to gather knowledge, to collect feedback and to encourage the collaboration. The ultimate goal being to drive the effort of the community toward the release of Scala 3.

All information you may want to know before starting the migration of your codebase should be available in this guide. If not you may want to [contribute](docs/contributing.md).

## Content

The [`docs/`](docs/) folder in this repository contains:
- [`compatibility.md`](docs/compatibility.md): The Scala 2 to Scala 3 compatibility reference.
- [`dotty-rewrites.md`](docs/dotty-rewrites.md): The documentation of the Dotty rewrite rules, that can be performed by the compiler.
- [`cross-build.md`](docs/cross-build.md): A tutorial for cross building your codebase. This is something that library maintainers would be interested in.
- [`upgrade.md`](docs/upgrade.md): Applications do not require cross building. You can jump straight and upgrade yours to Scala 3 by following this tutorial.
- [`macros.md`](docs/macros.md): General knowledge about migrating macros.

The [`incompat/`](incompat/) folder contains a corpus of incompatibilities between Scala 2 and Scala 3 and how they should translate from Scala 2.13 to Dotty.

In the near future it may be added:
- a list of the on-going initiatives to help the migration.

## Additional Resources

- [The Dotty website](https://dotty.epfl.ch/)
- [The Dotty example projects](https://github.com/lampepfl/dotty-example-project#getting-your-project-to-compile-with-dotty)
- [The Dotty community projects](https://github.com/lampepfl/dotty/tree/master/community-build/community-projects)
- [The Scalafix website](https://scalacenter.github.io/scalafix/)
- [The sbt Cross-Build Manual](https://www.scala-sbt.org/1.x/docs/Cross-Build.html)
