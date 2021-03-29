---
id: migration-tools
title: Tour of the Migration Tools
---

## The Scala Compilers

### The Scala 2.13 compiler

The Scala 2.13 compiler supports `-Xsource:3`, an option that enables some Scala 3 syntax and behavior:
- Most deprecated syntax generates an error.
- Infix operators can start a line in the middle of a multiline expression.
- Implicit search and overload resolution follow Scala 3 handling of contravariance when checking specificity.

The `-Xsource:3` option is intended to encourage early migration.

### The Scala 3 compiler

Similarly the Scala 3 compiler comes with the `-source:3.0-migration` option.
When activated the compiler accepts some Scala 2.13 syntax and it issues warnings where changes are needed.

Even more than that, it can be combined with `-rewrite` to rewrite your code automatically.

You can learn more about it in the [Scala 3 Migration Mode](scala-3-migration-mode.md) page.

## Build tools

### sbt

> The sbt-dotty plugin was needed in sbt 1.4 to get support for Scala 3.
> It is deprecated in sbt 1.5.

sbt 1.5 supports Scala 3 out-of-the-box.
All common tasks and settings are intended to work the same.
Many plugins should also work exactly the same.

To help with the migration, sbt 1.5 introduces new Scala 3 specific cross versions:

```scala
// build.sbt
libraryDependency += ("org.foo" %% "foo" % "1.0.0").cross(CrossVersion.for3Use2_13)
libraryDependency += ("org.bar" %% "bar" % "1.0.0").cross(CrossVersion.for2_13Use3)
```

### Mill

Mill 0.9.3 or greater supports Scala 3.

## Code editors and IDEs

### Metals

Metals is a Scala language server that works with VS Code, Vim, Emacs, Sublime Text and Eclipse.

Scala 3 is already very well supported by Metals.
Some minor adjustements for the new syntax changes and new features are coming. 

### IntelliJ IDEA

The Scala plugin for IntelliJ includes preliminary supports for Scala 3.
Full-fledged support is being worked on by the team at JetBrains.

## Migration Tools

### Scalafix

[Scalafix](https://scalacenter.github.io/scalafix/) is a refactoring tool for Scala.

The following incompatibilities can be solved with the corresponding Scalafix rules:
- Procedure Syntax: `ProcedureSyntax`
- Value Eta-Expansion: `fix.scala213.ExplicitNullaryEtaExpansion` in [scala/scala-rewrites](https://github.com/scala/scala-rewrites/blob/main/rewrites/src/main/scala/fix/scala213/ExplicitNullaryEtaExpansion.scala)
- Parentheses Around Lambda Paramter: `fix.scala213.ParensAroundLambda` in [ohze/scala-rewrites](https://github.com/ohze/scala-rewrites/blob/dotty/rewrites/src/main/scala/fix/scala213/ParensAroundLambda.scala)
- Auto Application: `fix.scala213.ExplicitNonNullaryApply` in [scala/scala-rewrites](https://github.com/scala/scala-rewrites/blob/main/rewrites/src/main/scala/fix/scala213/ExplicitNonNullaryApply.scala)
- `any2stringadd` Conversion: `fix.scala213.Any2StringAdd` in [scala/scala-rewrites](https://github.com/scala/scala-rewrites/blob/main/rewrites/src/main/scala/fix/scala213/Any2StringAdd.scala)

You can apply some of these rules using the `sbt-scalafix` plugin.
Or you can use the all-in-one `sbt-scala3-migrate` plugin described below.

### Scala 3 Migrate Plugin

Scala3 Migrate Plugin is an sbt plugin to assist you during the migration to Scala 3.

It proposes an incremental approach that can be described as follows:
- Migrate the library dependencies:
  For every library dependency it checks, if there are versions available for Scala 3.
- Migrate the Scala compiler options (`scalacOptions`):
  Some Scala 2 compiler options have been removed or renamed, others remain the same. 
  This step helps you adapt the compiler options of your project.
- Migrate the syntax:
  This step relies on Scalafix and existing rules to fix the deprecated syntax.
- Migrate the code by expliciting the types:
  Scala 3 has a new type inference algorithm that may infer slightly different types than the Scala 2 inference.
  This last step explicits a minimum set of types to make the project compiles in Scala 3 without changing its runtime behavior.

You can learn more in the [Scala 3 Migrate Plugin In Depth](scala-3-migrate-plugin.md) page.

## Scaladex

Check the list of Scala 3 open-source libraries in [Scaladex](https://index.scala-lang.org/).
