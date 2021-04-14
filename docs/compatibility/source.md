---
id: source
title: Source Level
---

A large subset of the Scala 2.13 language is still compatible in Scala 3.
Not all of it though, some constructs have been simplified, restricted or dropped altogether.
However those decisions were made for good reasons and by taking care that a good workaround is possible.

In general there is a straightforward cross-compiling solution for every incompatibility, so that the migration from Scala 2.13 to Scala 3 is easy and smooth.
You can find a corpus of incompatibilities in the [Incompatibility Table](../incompatibilities/incompatibility-table.md).

There is an exception though, which is the new metaprogramming framework that replaces the Scala 2 experimental macros.
Further explanations are given down below.

Metaprogramming aside, a Scala 2.13 source code can rather easily be ported to Scala 3.
Once done, you will be able to use the new powerful features of Scala 3, which have no equivalent in Scala 2.
The downside is those sources won't compile in Scala 2.13 anymore.
But amazingly, this new Scala 3 artifact can be consumed as a dependency in Scala 2.13.

As we will see in more detail, it permits forward compatibility on many new features, except the most exotic ones.
This is a breakthrough in the Scala programming history.
