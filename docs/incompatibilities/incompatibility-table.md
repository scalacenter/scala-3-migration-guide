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
|[Restricted keywords](syntactic-changes.md#restricted-keywords)||✅||The Scala 3 rule does not handle all cases|
|[Procedure syntax](syntactic-changes.md#procedure-syntax)|Deprecation|✅|[✅](https://scalacenter.github.io/scalafix/docs/rules/ProcedureSyntax.html)||
|[Parentheses around lambda parameter](syntactic-changes.md#parentheses-around-lambda-parameter)||✅|[✅](https://github.com/ohze/scala-rewrites/tree/dotty/#fixscala213parensaroundlambda)||
|[Open brace indentation for passing an argument](syntactic-changes.md#open-brace-indentation-for-passing-an-argument)||✅|||
|[Wrong indentation](syntactic-changes.md#wrong-indentation)||||Can be handled by a code formatting tool|
|[`_` as a type parameter](syntactic-changes.md#--as-a-type-parameter)|||||
|[`+` and `-` as type parameters](syntactic-changes.md#-and---as-type-parameters)|||||

### Dropped Features

Some features are dropped to simplify the language.

||Scala 2.13|Scala 3 Migration Rewrite|Scalafix Rule|Comments|
|--- |--- |--- |--- |--- |
|[Symbol literals](dropped-features.md#symbol-literals)|Deprecation|✅|||
|[`do`-`while` construct](dropped-features.md#do-while-construct)||✅|||
|[Auto-application](dropped-features.md#auto-application)|Deprecation|✅|[✅](https://github.com/scala/scala-rewrites/blob/main/rewrites/src/main/scala/fix/scala213/ExplicitNonNullaryApply.scala)||
|[Value eta-expansion](dropped-features.md#value-eta-expansion)|Deprecation|✅|[✅](https://github.com/scala/scala-rewrites/blob/main/rewrites/src/main/scala/fix/scala213/ExplicitNullaryEtaExpansion.scala)||
|[`any2stringadd` conversion](dropped-features.md#any2stringadd-conversion)|Deprecation||[✅](https://github.com/scala/scala-rewrites/blob/main/rewrites/src/main/scala/fix/scala213/Any2StringAdd.scala)||
|[Early initializer](dropped-features.md#early-initializer)|Deprecation||||
|[Existential type](dropped-features.md#existential-type)|Feature warning||||

### Contextual Abstractions

The redesign of [contextual abstractions](https://dotty.epfl.ch/docs/reference/contextual/motivation.html) brings some well defined incompatibilities.

||Scala 2.13|Scala 3 Migration Rewrite|Scalafix Rule|Comments|
|--- |--- |--- |--- |--- |
|[Type of implicit def](contextual-abstractions.md#type-of-implicit-definition)|||[✅](https://github.com/ohze/scala-rewrites#fixexplicittypesexplicitimplicittypes)||
|[Implicit views](contextual-abstractions.md#implicit-views)||||**Possible runtime incompatibility**|
|[View bounds](contextual-abstractions.md#view-bounds)|Deprecation||||
|[Ambiguous conversion on `A` and `=> A`](contextual-abstractions.md#ambiguous-conversion-on-a-and--a)|||||

Furthermore we have changed the implicit resolution rules so that they are more usefull and less surprising.
The new rules are described [here](https://dotty.epfl.ch/docs/reference/changed-features/implicit-resolution.html).

Because of these changes, the Scala 3 compiler could possibly fail at resolving some implicit parameters of existing Scala 2.13 code.
In practice, we discovered that those incompatibilities can often be fixed, if not always, by supplying some type parameters explicitly to help infer the type of the implicit parameter.
You will find some examples in the [Type Inference](type-inference.md) page.

### Other Changed Features

Some other features are simplified or restricted to make the language easier, safer or more consistent.

||Scala 2.13|Scala 3 Migration Rewrite|Scalafix Rule|Comments|
|--- |--- |--- |--- |--- |
|[Inheritance shadowing](other-changed-features.md#inheritance-shadowing)||✅|||
|[Non-private constructor in private class](other-changed-features.md#non-private-constructor-in-private-class)||Migration Warning|||
|[Abstract override](other-changed-features.md#abstract-override)|||||
|[Case class companion](other-changed-features.md#case-class-companion)|||||
|[Explicit call to unapply](other-changed-features.md#explicit-call-to-unapply)|||||
|[Invisible bean property](other-changed-features.md#invisible-bean-property)|||||
|[`=>T` as type argument](other-changed-features.md#-t-as-type-argument)|||||
|[Wildcard type argument](other-changed-features.md#wildcard-type-argument)|||||

### Type Checker

The Scala 2.13 type checker is unsound in some specific cases.
This can lead to surprising runtime errors in places we would not expect.
Scala 3 being based on stronger theoretical foundations, these unsoundess bugs in the type checker are now fixed.

||Scala 2.13|Scala 3 Migration Rewrite|Scalafix Rule|Comments|
|--- |--- |--- |--- |--- |
|[Variance checks](type-checker.md#unsoundness-fixes-in-variance-checks)|||||
|[Pattern matching](type-checker.md#unsoundness-fixes-in-pattern-matching)|||||

### Type Inference

Some specific type inference rules have changed between Scala 2.13 and Scala 3.

||Scala 2.13|Scala 3 Migration Rewrite|Scalafix Rule|Comments|
|--- |--- |--- |--- |--- |
|[Return type of override method](type-inference.md#inferred-return-type-of-an-override-method)|||||
|[Reflective type](type-inference.md#inferred-reflective-type)|||||

Also we have improved the type inference algorithm by redesigning it entirely.
This fundamental change leads to a few incompatibilities:
- A different type can be inferred
- A new type-checking error can appear

An unclassified corpus of known cases can be found in the [Type Inference](type-inference.md) page.

> It always is good practice to write the result types of all public values and methods explicitly.
> It prevents the plublic API of your library from changing with the Scala version, because of different inferred types.
> 
> This can be done prior to the Scala 3 migration by using the [ExplicitResultTypes](https://scalacenter.github.io/scalafix/docs/rules/ExplicitResultTypes.html) rule in Scalafix.

### Macros

The Scala 3 compiler is not able to expand Scala 2.13 macros.
Under such circumstances it is necessary to re-implement the Scala 2.13 macros using the new Scala 3 metaprogramming features.

You can go back to the [Metaprogramming](../compatibility/metaprogramming.md) page to learn about the new metaprogramming features.
