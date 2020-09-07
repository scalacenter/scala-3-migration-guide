---
id: dotty-rewrites
title: Dotty Migration Mode
---
## Usage

The Dotty compiler, named `dotc`, has been carefully designed to ease the migration from Scala 2.13 to Scala 3.0. It comes with a handful of utilities to encourage you to cross-compile your codebase:

### Migration mode

The `-source:3.0-migration` option makes the compiler forgiving on most of the dropped features, printing warnings in place of errors. Each warning is a strong indication that the the compiler is capable of safely rewriting the deprecated code into a cross-compiling code.

### Error explanations

The `-source:3.0-migration` mode handles many of the dropped constructs but not all of them. In some cases you will have some remaining errors due to incompatibilities between Scala 2 and Scala 3. The Dotty compiler will not be able to apply the automatic rewrites until you fix those errors. However it can assist you by providing detailed explanations on them. You can enable this by using `-source:3.0-migration` in combination with `-explain`.

This `-explain` option is not limited to migration, it is, in general a wonderful support to learn and code in Scala 3.

### Automatic rewrites

Once your code compiles with the `-source:3.0-migration`, all warnings can be resolved automatically by the addition of the `-rewrite` option. Beware that the compiler will modify your code! It is intended to be safe. However you may like to commit the initial state so that you can print the diff applied by the compiler and revert if necessary.

