---
id: incompatibility-table
title: Incompatibility Table
---

An incompatibility a piece of code that compiles in Scala 2.13 but does not compile in Scala 3.
Migrating a codebase involves finding and fixing all the incompatibilities of the source code.
On rare occasions we can also have a runtime incompatibility, that compiles in Scala 3 but results in a different runtime behavior.

In this page we propose a classification of the known incompatibilities.
Each incompatibility is described by the following pieces of information:
 - Its short name with a link towards the detailed description and proposed solutions
 - Whether the Scala 2.13 compiler produces a deprecation or a feature warning
 - The existence of a [Scala 3 migration](../tooling/scala-3-migration-mode.md) rule for it
 - The existence of a Scalafix rule that can fix it

> #### Scala 2.13 deprecations and feature warnings
> The below tables show which incompatibilitiy emits a warning when compiled in Scala 2.13:
> - Add the `-deprecation` compiler option to locate the usage of deprecated APIs
> - For locating the feature warnings, you can look for the feature specific `import` and/or add the `-feature` compiler option.

> #### Scala 3 migration and Scalafix rewrites
> The Scala 3 migration mode is fully integrated in the Scala 3 compiler.
> On the contrary, Scalafix is a tool that must be installed and configured.
> However Scalafix has its own advantages:
> - It runs on Scala 2.13.
> - It is composed of individual rules that you can apply one at a time.
> - It is easily extensible by adding custom rules.

### Syntactic Changes

Some of the old syntax is not supported anymore.

||Scala 2.13|Scala 3 Migration Rewrite|Scalafix Rule|Comments|
|--- |--- |--- |--- |--- |
|[Restricted keywords](../incompatibilities/syntactic-changes.md#restricted-keywords)||✅||The Scala 3 rule does not handle all cases|
|[Procedure syntax](../incompatibilities/syntactic-changes.md#procedure-syntax)|Deprecation|✅|✅||
|[Parentheses around lambda parameter](../incompatibilities/syntactic-changes.md#parentheses-around-lambda-parameter)||✅|✅||
|[Open brace indentation for passing an argument](../incompatibilities/syntactic-changes.md#open-brace-indentation-for-passing-an-argument)||✅|||
|[Wrong indentation](../incompatibilities/syntactic-changes.md#wrong-indentation)||||Can be handled by a code formatting tool|
|[`_` as a type parameter](../incompatibilities/syntactic-changes.md#--as-a-type-parameter)|||||
|[`+` and `-` as type parameters](../incompatibilities/syntactic-changes.md#-and---as-type-parameters)|||||

### Dropped Features

Some features are dropped to simplify the language.

||Scala 2.13|Scala 3 Migration Rewrite|Scalafix Rule|Comments|
|--- |--- |--- |--- |--- |
|[Symbol literals](../incompatibilities/dropped-features.md#symbol-literals)|Deprecation|✅|||
|[`do`-`while` construct](../incompatibilities/dropped-features.md#do-while-construct)||✅|||
|[Auto-application](../incompatibilities/dropped-features.md#auto-application)|Deprecation|✅|✅||
|[Value eta-expansion](../incompatibilities/dropped-features.md#value-eta-expansion)|Deprecation|✅|✅||
|[`any2stringadd` conversion](../incompatibilities/dropped-features.md#any2stringadd-conversion)|Deprecation||✅||
|[Early initializer](../incompatibilities/dropped-features.md#early-initializer)|Deprecation||||
|[Existential type](../incompatibilities/dropped-features.md#existential-type)|Feature warning||||

### Contextual Abstractions

The redesign of [contextual abstractions](https://dotty.epfl.ch/docs/reference/contextual/motivation.html) introduces the following incompatibilities:

||Scala 2.13|Scala 3 Migration Rewrite|Scalafix Rule|Comments|
|--- |--- |--- |--- |--- |
|[Type of implicit def](../incompatibilities/contextual-abstractions.md#type-of-implicit-definition)|||✅||
|[Implicit views](../incompatibilities/contextual-abstractions.md#implicit-views)||||Possible runtime incompatibility|
|[View bounds](../incompatibilities/contextual-abstractions.md#view-bounds)|Deprecation||||
|[Ambiguous conversion on `A` and `=> A`](../incompatibilities/contextual-abstractions.md#ambiguous-conversion-on-a-and--a)|||||

### Other Changed Features

Some features are simplified or restricted to make the language easier and safer to use.

||Scala 2.13|Scala 3 Migration Rewrite|Scalafix Rule|Comments|
|--- |--- |--- |--- |--- |
|[Inheritance shadowing](../incompatibilities/other-changed-features.md#inheritance-shadowing)||✅|||
|[Abstract override](../incompatibilities/other-changed-features.md#abstract-override)|||||
|[`ExprType` as value type](../incompatibilities/other-changed-features.md#exprtype-as-value-type)|||||
|[Explicit call to unapply](../incompatibilities/other-changed-features.md#explicit-call-to-unapply)|||||
|[Non-private constructor in private class](../incompatibilities/other-changed-features.md#non-private-constructor-in-private-class)||warning|||
|[Reflective call](../incompatibilities/other-changed-features.md#reflective-call)||||Type inference problem|
|[Wildcard type argument](../incompatibilities/other-changed-features.md#wildcard-type-argument)|||||
|[Case class companion](../incompatibilities/other-changed-features.md#case-class-companion)|||||
|[Unsoundness fixes in variance checks](../incompatibilities/other-changed-features.md#unsoundness-fixes-in-variance-checks)|||||
|[Unsoundness fixes in pattern matching](../incompatibilities/other-changed-features.md#unsoundness-fixes-in-pattern-matching)|||||

### Implicit Resolution

We changed the implicit resolution rules to make them more useful and less surprising. The new rules are described [here](https://dotty.epfl.ch/docs/reference/changed-features/implicit-resolution.html).

Because of these changes, the Scala 3 compiler could possibly fail at resolving some implicit parameters of existing Scala 2.13 code.
Even worse, it could resolve a different value which would silently change the behavior of the program.
However we believe these cases are rare or inexistent.

### Type Inference

The Scala 3 compiler uses a new type inference algorithm that is better than the Scala 2.13 one.

This fundamental change in Scala 3 leads to a few incompatibilities:
- A different type can be inferred
- A new type-checking error can appear

> It is good practice to write the result types of all public values and methods explicitly.
> It prevents the plublic API of your library from changing with the Scala version, because of different inferred types.

### Macros

The Scala 3 compiler is not able to consume the Scala 2.13 macros.
Under those circumstances it is necessary to re-implement the Scala 2.13 macros using the new Scala 3 metaprogramming features.

You can go to the [Metaprogramming](metaprogramming.md) page to learn about the new metaprogramming features.
To learn how to make a macro library available in Scala 3 you can follow the [Porting a Macro Library](../macros/migration-tutorial.md) tutorial.

Some of the most used macro libraries have already been migrated to Scala 3.
Check the list of [Scala macro libraries](../macros/macro-libraries.md).
