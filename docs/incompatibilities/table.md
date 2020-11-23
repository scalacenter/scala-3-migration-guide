---
id: table
title: Incompatibility Table
---

> This table is built upon @scala30@.
> Some changes might have occurred on more recent versions of the Scala 3.0 compiler.

We call incompatibility a piece of code that compiles in Scala 2.13 but does not compile in Scala 3.0.
Migrating a codebase involves finding and fixing all the incompatibilities of the source code.
On rare occasions we can also have runtime incompatibilities, that compile in Scala 3.0 but result in a different runtime behavior.

In this page we propose a classification and status of the known incompatibilities.
The status of an incompatibility is comprised of:
 - Whether the Scala 2.13 compiler produces a deprecation or feature warning on it
 - The existence of a [Scala 3.0 migration](../scala-3-migration-mode.md) rule for it
 - The existence of a Scalafix rule that can fix it

> #### Scala 2.13 deprecations and feature warnings
>
> The below tables show which incompatibilitiy can emit a warning when compiled in Scala 2.13:
> - Add the `-deprecation` compiler option to locate the usage of deprecated APIs
> - For locating the feature warnings, you can look for the feature specific `import` and/or add the `-feature` compiler option.

> #### Scala 3.0 migration and Scalafix rewrites
> The Scala 3.0 migration mode is fully integrated in the Scala 3.0 compiler.
> On the contrary, Scalafix is a tool that must be installed and manually configured in your project.
> However Scalafix has its own advantages:
> - It runs on Scala 2.13.
> - It is composed of individual rules that you can apply one at a time.
> - It is easily extensible by adding custom rules.

## Syntactic Changes

Some of the old Scala syntax is not supported anymore.

||Scala 2.13|Scala 3.0 Migration Rewrite|Scalafix Rule|Comments|
|--- |--- |--- |--- |--- |
|[Restricted keywords](syntactic-changes.md#restricted-keywords)||✅||The Scala 3.0 rule does not handle all cases|
|[Procedure syntax](syntactic-changes.md#procedure-syntax)|Deprecation|✅|✅||
|[Parentheses around lambda parameter](syntactic-changes.md#parentheses-around-lambda-parameter)||✅|✅||
|[Open brace indentation for passing an argument](syntactic-changes.md#open-brace-indentation-for-passing-an-argument)||✅|||
|[Wrong indentation](syntactic-changes.md#wrong-indentation)||||Can be handled by a code formatting tool|
|[`_` as a type parameter](syntactic-changes.md#--as-a-type-parameter)|||||
|[`+` and `-` as type parameters](syntactic-changes.md#-and---as-type-parameters)|||||

## Dropped Features

Some features are dropped to simplify the language.

||Scala 2.13|Scala 3.0 Migration Rewrite|Scalafix Rule|Comments|
|--- |--- |--- |--- |--- |
|[Symbol literals](dropped-features.md#symbol-literals)|Deprecation|✅|||
|[`do`-`while` construct](dropped-features.md#do-while-construct)||✅|||
|[Auto-application](dropped-features.md#auto-application)|Deprecation|✅|✅||
|[Value eta-expansion](dropped-features.md#value-eta-expansion)|Deprecation|✅|✅||
|[`any2stringadd` conversion](dropped-features.md#any2stringadd-conversion)|Deprecation||✅||
|[Early initializer](dropped-features.md#early-initializer)|Deprecation||||
|[Existential type](dropped-features.md#existential-type)|Feature warning||||

## Contextual Abstractions

The redesign of [contextual abstractions](https://dotty.epfl.ch/docs/reference/contextual/motivation.html) in Scala 3 introduces the following incompatibilities:

||Scala 2.13|Scala 3.0 Migration Rewrite|Scalafix Rule|Comments|
|--- |--- |--- |--- |--- |
|[Type of implicit def](contextual-abstractions.md#type-of-implicit-definition)|||✅||
|[Implicit views](contextual-abstractions.md#implicit-views)||||Possible runtime incompatibility|
|[View bounds](contextual-abstractions.md#view-bounds)|Deprecation||||
|[Ambiguous conversion on `A` and `=> A`](contextual-abstractions.md#ambiguous-conversion-on-a-and--a)|||||

## Other Changed Features

Some proven features are simplified or restricted to make the language easier and safer to use.

||Scala 2.13|Scala 3.0 Migration Rewrite|Scalafix Rule|Comments|
|--- |--- |--- |--- |--- |
|[Inheritance shadowing](other-changed-features.md#inheritance-shadowing)||✅|||
|[Abstract override](other-changed-features.md#abstract-override)|||||
|[`ExprType` as value type](other-changed-features.md#exprtype-as-value-type)|||||
|[Variance of default parameters](other-changed-features.md#variance-of-default-parameters)|||||
|[Explicit call to unapply](other-changed-features.md#explicit-call-to-unapply)|||||
|[`java.lang.Enum`](other-changed-features.md#javalangenum)|||||
|[Non-private constructor in private class](other-changed-features.md#non-private-constructor-in-private-class)||warning|||
|[Reflective call](other-changed-features.md#reflective-call)||||Type inference problem|
|[Wildcard type argument](other-changed-features.md#wildcard-type-argument)|||||


## Implicit Resolution

The implicit resolution rules have been cleaned up to make them more useful and less surprising.

Since the rules are different, Scala 3.0 can fail at resolving some implicit parameters of existing Scala 2.13 code.
Even worse, it can resolve a different value than the one resolved by the Scala 2.13 compiler, which would silently change the behavior of the program.
However we believe these cases are rare or inexistent.

## Type Inference

The Scala 3.0 compiler uses a new type inference algorithm that is better than the old one.

This fundamental change in Scala 3 leads to a few incompatibilities:
- The Scala 3.0 compiler can infer a different type than the one inferred by the Scala 2.13 compiler
- The Scala 3.0 compiler can diagnose a type-checking error where the Scala 2.13 compiler does not

> It is good practice to write the result types of all public values and methods explicitly.
> It indeed prevents the API from changing, from a Scala version to another, because of a different inferred type.

> #### From Scala 2.13 to Scala 3.0 Typer and Implicit Resolver
> 
> Given the complexity of the type inference and implicit resolution algorithms it is hard to predict the incompatibilities.
> 
> The Scala Center is prototyping a tool that can detect and fix the type inference and implicit resolution incompatibilities.
> While being experimental, this project has the potential to migrate large codebases and to identify new patterns of incompatibilities.
> 
> You can check the roadmap and the progress on the [Scala contributor forum](https://contributors.scala-lang.org/t/the-scala-2-to-scala-3-typer-and-implicit-resolver/4446).

## Macros

The Scala 3.0 compiler is not able to compile or consume Scala 2.13 macro methods.
Under those circumstances it is necessary to re-implement the Scala 2.13 macros by using the new Scala 3.0 metaprogramming features.

You can go to the [Metaprogramming in Scala 3](../macros/metaprogramming.md) page to learn about the new metaprogramming features.
To learn how to make a macro library available in Scala 3.0 you can read the [Migrating a Macro Library](../macros/migration-tutorial.md) tutorial.

Some of the most used macro libraries have already been migrated to Scala 3.0.
Check the list of [Scala macro libraries](../macros/macro-libraries.md).
