---
id: incompatibility-table
title: Incompatibility Table
---

We call incompatibility a piece of code that compiles in Scala 2.13 but does not compile in Scala 3.0.
Migrating a codebase involves finding and fixing all the incompatibilities of the source code.
On rare occasions we can also have runtime incompatibilities, that compile in Scala 3.0 but result in a different runtime behavior.

In this page we propose a classification and status of the known incompatibilities.
The status of an incompatibility is comprised of:
 - Whether the Scala 2.13 compiler produces a deprecation or feature warning on it.
 - The existence of a [Dotty migration](dotty-rewrites.md) rule for it.
 - The existence of a Scalafix rule that can fix it.

> #### Scala 2.13 deprecations and feature warnings
>
> The below tables show which incompatibilitiy can emit a warning when compiled in Scala 2.13:
> - Add the `-deprecation` compiler option to locate the usage of deprecated APIs
> - For locating the feature warnings, you can look for the feature specific `import` and/or add the `-feature` compiler option.

> #### Dotty migration and Scalafix rewrites
> The Dotty migration mode is fully integrated in the Dotty compiler.
> On the contrary, Scalafix is a tool that must be installed and manually configured in your project.
> However Scalafix has its own advantages:
> - It runs on Scala 2.13.
> - It is composed of individual rules that you can apply one at a time.
> - It is easily extensible by adding custom rules.

## Syntactic Changes

Some of the old Scala syntax is not supported anymore.

<table>
  <thead>
    <tr>
      <th/>
      <th style="text-align:center">Scala 2.13</th>
      <th style="text-align:center">Dotty Migration Rewrite</th>
      <th style="text-align:center">Scalafix Rule</th>
      <th style="text-align:center">Comments</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td style="text-align:left">Keyword as identifier</td>
      <td/>
      <td style="text-align:center">✅</td>
      <td/>
      <td style="text-align:left">The Dotty rule does not handle <code>=>></code></td>
    </tr>
    <tr>
      <td style="text-align:left">Procedure syntax</td>
      <td style="text-align:left">Deprecation</td>
      <td style="text-align:center">✅</td>
      <td style="text-align:center">✅</td>
      <td/>
    </tr>
    <tr>
      <td style="text-align:left">Parentheses around lambda parameter</td>
      <td/>
      <td style="text-align:center">✅</td>
      <td style="text-align:center">✅</td>
      <td/>
    </tr>
    <tr>
      <td style="text-align:left">Open brace indentation for passing an argument</td>
      <td/>
      <td style="text-align:center">✅</td>
      <td/>
      <td/>
    </tr>
    <tr>
      <td style="text-align:left">Wrong indentation</td>
      <td/>
      <td/>
      <td/>
      <td style="text-align:left">Can be handled by scalafmt</td>
    </tr>
    <tr>
      <td style="text-align:left"><code>_</code> as a type parameter</td>
      <td/>
      <td/>
      <td/>
      <td/>
    </tr>
    <tr>
      <td style="text-align:left"><code>+</code> and <code>-</code> as type parameters</td>
      <td/>
      <td/>
      <td/>
      <td/>
    </tr>
  </tbody>
</table>

## Dropped Features

Some features are dropped to simplify the language.

<table>
  <thead>
    <tr>
      <th/>
      <th style="text-align:center">Scala 2.13</th>
      <th style="text-align:center">Dotty Migration Rewrite</th>
      <th style="text-align:center">Scalafix Rule</th>
      <th style="text-align:center">Comments</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td style="text-align:left">Symbol literal</td>
      <td style="text-align:left">Deprecation</td>
      <td style="text-align:center">✅</td>
      <td/>
      <td/>
    </tr>
    <tr>
      <td style="text-align:left"><code>do</code>-<code>while</code> construct</td>
      <td/>
      <td style="text-align:center">✅</td>
      <td/>
      <td/>
    </tr>
    <tr>
      <td style="text-align:left">Auto-application</td>
      <td style="text-align:left">Deprecation</td>
      <td style="text-align:center">✅</td>
      <td style="text-align:center">✅</td>
      <td/>
    </tr>
    <tr>
      <td style="text-align:left">Value eta-expansion</td>
      <td style="text-align:left">Deprecation</td>
      <td style="text-align:center">✅</td>
      <td style="text-align:center">✅</td>
      <td/>
    </tr>
    <tr>
      <td style="text-align:left">
        <code>Any2StringAdd</code> conversion
      </td>
      <td style="text-align:left">Deprecation</td>
      <td/>
      <td style="text-align:center">✅</td>
      <td/>
    </tr>
    <tr>
      <td style="text-align:left">Early initializer</td>
      <td/>
      <td/>
      <td/>
      <td/>
    </tr>
    <tr>
      <td style="text-align:left">Existential type</td>
      <td style="text-align:left">Feature warning</td>
      <td/>
      <td/>
      <td/>
    </tr>
  </tbody>
</table>

## Contextual Abstraction

The redesign of [contextual abstractions](https://dotty.epfl.ch/docs/reference/contextual/motivation.html) in Scala introduces the following incompatibilities:

<table>
  <thead>
    <tr>
      <th/>
      <th style="text-align:center">Scala 2.13</th>
      <th style="text-align:center">Dotty Migration Rewrite</th>
      <th style="text-align:center">Scalafix Rule</th>
      <th style="text-align:center">Comments</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td style="text-align:left">Type of implicit def</td>
      <td/>
      <td/>
      <td style="text-align:center">✅</td>
      <td/>
    </tr>
    <tr>
      <td style="text-align:left">Implicit view</td>
      <td/>
      <td/>
      <td/>
      <td style="text-align:left">- Migration warning<br/>- Possible runtime incompatibility</td>
    </tr>
    <tr>
      <td style="text-align:left">View bound</td>
      <td style="text-align:left">Deprecation</td>
      <td/>
      <td/>
      <td/>
    </tr>
    <tr>
      <td style="text-align:left">Ambiguous conversion between <code>A => B</code> and <code>=> A => B</code></td>
      <td/>
      <td/>
      <td/>
      <td/>
    </tr>
  </tbody>
</table>

## Other Changed Features

Some proven features are simplified or restricted to make the language easier and safer to use.

<table>
  <thead>
    <tr>
      <th/>
      <th style="text-align:center">Scala 2.13</th>
      <th style="text-align:center">Dotty Migration Rewrite</th>
      <th style="text-align:center">Scalafix Rule</th>
      <th style="text-align:center">Comments</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td style="text-align:left">Inheritance shadowing</td>
      <td/>
      <td style="text-align:center">✅</td>
      <td/>
      <td/>
    </tr>
    <tr>
      <td style="text-align:left">Abstract override</td>
      <td/>
      <td/>
      <td/>
      <td/>
    </tr>
    <tr>
      <td style="text-align:left"><code>ExprType</code> as value type</td>
      <td/>
      <td/>
      <td/>
      <td/>
    </tr>
    <tr>
      <td style="text-align:left">Variance of a default parameter</td>
      <td/>
      <td/>
      <td/>
      <td/>
    </tr>
    <tr>
      <td style="text-align:left">Explicit call to <code>unapply</code></td>
      <td/>
      <td/>
      <td/>
      <td/>
    </tr>
    <tr>
      <td style="text-align:left"><code>java.lang.Enum</code></td>
      <td/>
      <td/>
      <td/>
      <td/>
    </tr>
    <tr>
      <td style="text-align:left">Reflective call</td>
      <td/>
      <td/>
      <td/>
      <td style="text-align:left">
        Soon fixed by
        <a href="https://github.com/lampepfl/dotty/pull/9420">#9420</a>
      </td>
    </tr>
  </tbody>
</table>

## Implicit Resolution

The implicit resolution rules have been cleaned up to make them more useful and less surprising.

Since the rules are different, Scala 3.0 can fail at resolving some implicit parameters of existing Scala 2 code.
Even worse, it can resolve a different value than the one resolved by Scala 2.13, which would silently change the behavior of the program.
However we believe these cases are rare or inexistent.

## Type Inference

The Scala 3 compiler uses a new type inference algorithm that is better than the old one.

This fundamental change in Dotty leads to a few incompatibilities:
- The Scala 3 compiler can infer a different type than the one inferred by the Scala 2 compiler
- The Scala 3 compiler can diagnose a type-checking error where the Scala 2 compiler does not

> #### From Scala 2 to Scala 3 Typer and Implicit resolver
> 
> Given the complexity of the type inference and implicit resolution algorithms it is hard to predict the incompatibilities.
> 
> The Scala Center is prototyping a tool that can detect and fix the type inference and implicit resolution incompatibilities.
> While being experimental, this project will help us to migrate large codebases and to identify new patterns of incompatibilities.
> 
> You can check the roadmap and the progress on the [Scala contributor forum](https://contributors.scala-lang.org/t/the-scala-2-to-scala-3-typer-and-implicit-resolver/4446).

## Macros

The Scala 3 compiler is not able to compile or consume a Scala 2 macro method.
Under those circumstances it is necessary to re-implement the Scala 2 macro implementations by using the new Scala 3 metaprogramming features.

You can redirect to the [Macros Migration](macros.md) page to learn how to do so.

Some of the most used metaprogramming libraries have already been migrated to Scala 3. Check the list of the [Scala 3 metaprogramming libraries](macros.md#migration-status).
