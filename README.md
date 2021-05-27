# Scala 3 migration guide

> The content of the Scala 3 migration guide has moved to [docs.scala-lang.org](https://docs.scala-lang.org/scala3/guides/migration/compatibility-intro.html)

This repository is a continuously evolving guide to support the migration to Scala 3.
As a member of the community you are encouraged to participate the effort by sharing your migrating experience.

Visit [**the Scala 3 Migration website**](https://scalacenter.github.io/scala-3-migration-guide) to learn more about the migration.

A number of complementary initiatives are being undertaken to ease the migration.
This repository is not a replacement of those initiatives.
Its purpose is to gather knowledge, to collect feedback and to encourage the collaboration.
The ultimate goal being to drive the effort of the community toward the release of Scala 3.0.0.

All information you may want to know before starting the migration of your codebase should be available in this guide.
If not you may want to [contribute](docs/contributing.md).

## Content

This repository contains:
 - [`incompat-30/`](incompat-30/): A corpus of incompatibilities between Scala 2.13 and Scala 3.0 with proposed solution. It also contains the tests of the Scala 3.0 migration rewrites for 3.0.
 - [`incompat-31/`](incompat-31/): The tests of the Dotty migration rewrites that are already implemented for 3.1.
 - [`docs/`](docs/): The documentation that is still published to the [deprecated website](https://scalacenter.github.io/scala-3-migration-guide/)
 - [`website/`](website/): The website skeleton powered by [Docusaurus](https://docusaurus.io/en/).

## Additional Resources

- [The Scala website](https://scala-lang.org/)
- [The Dotty website](https://dotty.epfl.ch/)
- [The Scala 3 example project](https://github.com/lampepfl/scala3-example-project)
- [The Scala 3 community projects](https://github.com/lampepfl/dotty/tree/master/community-build/community-projects)
- [The Scala 3 Migration Plugin](https://github.com/scalacenter/scala3-migrate) for sbt
