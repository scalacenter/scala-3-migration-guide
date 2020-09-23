---
id: dotty-rewrites
title: Scala 3 Migration Mode
---

The Scala 3 compiler, named `dotc`, has been carefully designed to ease the migration from Scala 2.13 to Scala 3.0.
It comes with a handful of utilities to support you while migrating a Scala 2 codebase to Scala 3.

Try running `dotc`, the Scala 3 compiler command, to have a glimpse of those utilities:

``` bash
$ dotc
Usage: dotc <options> <source files>
where possible standard options include:

...
-explain           Explain errors in more detail.
-explain-types     Explain type errors in more detail.
...
-rewrite           When used in conjunction with a `...-migration` source version, rewrites sources to migrate to new version.
...
-source            source version
                   Default: 3.0.
                   Choices: 3.0, 3.1, 3.0-migration, 3.1-migration.
...
```

## Migration mode

The `-source:3.0-migration` option makes the compiler forgiving on most of the dropped features, printing warnings in place of errors.
Each warning is a strong indication that the the compiler is even capable of safely rewriting the deprecated piece of code into a cross-compiling one.

We call this mode the **Scala 3 Migration Mode**.

## Automatic rewrites

Once your code compiles in the migration mode, almost all warnings can be resolved automatically by the compiler itself.
To do so you just need to compile again, this time with the `-source:3.0-migration` and the `-rewrite` options.

> Beware that the compiler will modify the code! It is intended to be safe.
> However you may like to commit the initial state so that you can print the diff applied by the compiler and revert it if necessary.

> #### Good to know
> - the rewrites are not applied if the code compiles in error
> - you cannot choose which rules are applied, the compiler runs all of them

You can refer to the [incompatibility table](incompatibilities/table.md) to see the list of Scala 3 migration rewrites.

## Error explanations

The `-source:3.0-migration` mode handles many of the changed features but not all of them.
In some cases you can have remaining errors due to incompatibilities between Scala 2 and Scala 3.
The Scala 3 compiler can assist you by providing detailed explanations on them.
You can enable this by using `-source:3.0-migration` in combination with `-explain` and `-explain-types`.

> The `-explain` and `-explain-types` options are not limited to the migration mode.
> They can, in general, assist you to learn and code in Scala 3.
