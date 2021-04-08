---
id: scalacoptions-migration
title: Porting the scalacOptions
---

As part of migrating the build (`build.sbt`), you will need to adapt the scalacOptions
for Scala 3. In fact, between Scala 2.13 and Scala 3.0, the available compiler options are different:
- Some Scala 2.13 options are not supported by the Scala 3.0 compiler.
- New options are available to enable new features of the Scala 3.0 compiler.

The goal is to end up with a configuration that will look like this:
```scala
// build.sbt
scalacOptions ++= {
  if (isDotty.value) Seq(
    "-encoding",
    "UTF-8",
    "-feature",
    "-unchecked",
    "-language:implicitConversions"
    // "-Xfatal-warnings" will be added after the migration
  ) else Seq(
    "-encoding",
    "UTF-8",
    "-feature",
    "-deprecation",
    "-language:implicitConversions",
    "-Xfatal-warnings",
    "-Wunused:imports,privates,locals",
    "-Wvalue-discard"
  )
}
```

>
> The comparison is based on Scala 2.13.4 and 3.0.0-M3 
> 

Go through the following sections to discover which scalacOptions are shared, new, or unavailable in Scala 3

1. [Shared scalacOptions between Scala 2 and scala 3](#1---shared-scalacoptions-between-scala-2-and-scala-3)
2. [Unavailable scalacOptions in Scala 3](#2---unavailable-scalacoptions-in-scala-3)
3. [New scalacOptions in Scala 3](#3---new-scalacoptions-in-scala-3)

## 1 - Shared scalacOptions between Scala 2 and scala 3

This is the list of scalacOptions shared between Scala 2 and Scala 3.
Some of them have been renamed as indicated in the table below.

### 1. Standard settings

| 2.13.4 | 3.0.x |
|-|-|
| `-P:<plugin>:<opt>` | Same | 
| `-bootclasspath` | Same | 
| `-classpath` | Same | 
| `-d` | Same | 
| `-deprecation` | Same | 
| `-encoding` | Same | 
| `-explaintypes` | `-explain-types` | 
| `-extdirs` | Same | 
| `-feature` | Same | 
| `-help` | Same | 
| `-javabootclasspath` | Same | 
| `-javaextdirs` | Same | 
| `-language` | Same | 
| `-nowarn` | Same | 
| `-print` | Same | 
| `-release` | Same | 
| `-Xsource` | `-source` |
| `-sourcepath` | Same | 
| `-target` | `-Xtarget` | 
| `-unchecked` | Same | 
| `-uniqid` | Same | 
| `-usejavacp` | Same | 
| `-verbose` | Same | 
| `-version` | Same | 

### 2. Advanced settings

| 2.13.4 | 3.0.x |
|-|-|
|`-X` | Same |
|`-Xcheckinit` | `-Ycheck-init` | 
|`-Xmigration` | Same | 
|`-Xmixin-force-forwarders` | Same | 
|`-Xno-forwarders` | Same | 
|`-Xplugin` | Same | 
|`-Xplugin-disable` | Same | 
|`-Xplugin-list` | Same | 
|`-Xplugin-require` | Same | 
|`-Xpluginsdir` | Same | 
|`-Xprompt` | Same | 
|`-Xverify` | `-Xverify-signatures` |
|`-Vprint-types` | `-Xprint-types` | 

### 3. Private settings

| 2.13.4 | 3.0.x |
|-|-|
|`-Y` | Same | 
|`-Ycheck` | Same | 
|`-Ydump-classes` | Same | 
|`-Yno-generic-signatures` | Same | 
|`-Yno-imports` | Same | 
|`-Yno-predef` | Same | 
|`-Yprofile-destination` | Same | 
|`-Yprofile-enabled` | Same | 
|`-Yresolve-term-conflict` | Same | 
|`-Yskip` | Same | 
|`-Ystop-after` | Same | 
|`-Ystop-before` | Same | 

### 4. Scala.js settings

| 2.13.4 | 3.0.x |
|-|-|
|`-P:scalajs:genStaticForwardersForNonTopLevelObjects` |`-scalajs-genStaticForwardersForNonTopLevelObjects` |
|`-P:scalajs:mapSourceURI`| `-scalajs-mapSourceURI`|


### 5. Warning and verbose settings
Warning and verbose settings have been introduced in 2.13. They are not yet implemented in Scala 3.0.x except

| 2.13.4 | 3.0.x |
|-|-|
| `-Werror` | `-Xfatal-warnings` |
| `-Xlint:deprecation` | `-deprecation` |
| `-Vprint:<phases>` | `-Xprint:<phases>` |
| `-Vphases` | `-Xshow-phases` |
| `-Vclasspath` | `-Ylog-classpath` |
| `-Vlog:<phases>` | `-Ylog:<phases>`|
| `-Vdebug` | `-Ydebug` |
| `-Vprint-pos` | `-Yprint-pos` |


## 2 - Unavailable scalacOptions in Scala 3
Some scalacOptions no longer exist in scala 3. If those scalacOptions are added it produces a warning message
`bad option 'Option' was ignored`. It won't prevent from starting the compilation, but the options are ignored.

### 1. Unavailable standard settings

| 2.13.4 |
|-|
| `-Dproperty=value` |
| `-J<flag>` |
| `-dependencyfile` |
| `-g` |
| `-no-specialization` |
| `-nobootcp` |
| `-opt` |
| `-opt-inline-from` |
| `-opt-warnings` |
| `-optimize` |
| `-rootdir` |
| `-toolcp` |
| `-usemanifestc` |

### 2. Unavailable warning and verbose settings
Warning and verbose settings have been introduced in 2.13. They are not yet implemented in Scala 3.0.x

| 2.13.4 |
|-|
| `-W` |
| `-Wconf` |
| `-Wdead-code` or `-Ywarn-dead-code` |
| `-Wextra-implicit` |
| `-Wmacros:<mode>` |
| `-Wnumeric-widen`  or `-Ywarn-numeric-widen`|
| `-Woctal-literal` |
| `-Wunused:WARNING1,WARNING2` or `-Ywarn-unused:WARNING1,WARNING2`|
| `-Wvalue-discard` or `-Ywarn-value-discard`|
| `-Xlint:WARNING1,WARNING2` |
| `-Wself-implicit` |
| `-V` |
| `-Vbrowse:<phases>` |
| `-Vdebug-tasty` |
| `-Vdoc` |
| `-Vfree-terms` |
| `-Vfree-types` |
| `-Vhot-statistics` or `-Yhot-statistics`|
| `-Vide` or `-Yide-debug`|
| `-Vimplicit-conversions` or `-Xlog-implicit-conversion`|
| `-Vimplicits` or `-Xlog-implicits`|
| `-Vinline <package/Class.method>` or `-Yopt-log-inline <package/Class.method>` |
| `-Vissue`  or `-Yissue-debug`|
| `-Vmacro` or `-Ymacro-debug-verbose`|
| `-Vmacro-lite` or `-Ymacro-debug-lite`|
| `-Vopt <package/Class.method>` or `-Yopt-trace <package/Class.method>`|
| `-Vpatmat` or `-Ypatmat-debug`|
| `-Vpos` or `-Ypos-debug`|
| `-Vprint-args <file>` or `-Xprint-args`|
| `-Vquasiquote` or `Yquasiquote-debug` |
| `-Vreflective-calls` or `-Xlog-reflective-calls` |
| `-Vreify` or `-Yreify-debug`|
| `-Vshow:<phases>` or `-Yshow:<phases>` |
| `-Vshow-class <class>` or `-Xshow-class <class>` |
| `-Vshow-member-pos <output style>` or `-Yshow-member-pos <output style>` |
| `-Vshow-object <object>` or `-Xshow-object <object>` |
| `-Vshow-symkinds` or `-Yshow-symkinds` |
| `-Vshow-symowners` or `-Yshow-symowners`|
| `-Vstatistics <phases>  -Ystatistics <phases>`|
| `-Vsymbols` or `-Yshow-syms` |
| `-Vtyper` or `-Ytyper-debug` |

### 3. Unavailable advanced settings

| 2.13.4 |
|-|
| `-Xdev` |
| `-Xdisable-assertions` |
| `-Xelide-below` |
| `-Xexperimental` |
| `-Xfuture` |
| `-Xgenerate-phase-graph` |
| `-Xjline` |
| `-Xmacro-settings` |
| `-Xmain-class` |
| `-Xmaxerrs` |
| `-Xmaxwarns` |
| `-Xno-patmat-analysis` |
| `-Xnojline` |
| `-Xreporter` |
| `-Xresident` |
| `-Xscript` |
| `-Xsource-reader` |
| `-Xxml` |


### 4. Unavailable private settings

| 2.13.4 |
|-|
| `-Ybackend-parallelism` |
| `-Ybackend-worker-queue` |
| `-Ybreak-cycles` |
| `-Ycache-macro-class-loader` |
| `-Ycache-plugin-class-loader` |
| `-Ycompact-trees` |
| `-Ydelambdafy` |
| `-Ygen-asmp` |
| `-Yimports` |
| `-Yjar-compression-level` |
| `-YjarFactory` |
| `-Ymacro-annotations` |
| `-Ymacro-classpath` |
| `-Ymacro-expand` |
| `-Ymacro-global-fresh-names` |
| `-Yno-completion` |
| `-Yno-flat-classpath-cache` |
| `-Yopt-inline-heuristics` |
| `-Ypatmat-exhaust-depth` |
| `-Ypresentation-any-thread` |
| `-Ypresentation-debug` |
| `-Ypresentation-delay` |
| `-Ypresentation-locate-source-file` |
| `-Ypresentation-log` |
| `-Ypresentation-strict` |
| `-Ypresentation-verbose` |
| `-Yprint-trees` |
| `-Yprofile-trace` |
| `-Yrangepos` |
| `-Yrecursion` |
| `-Yreify-copypaste` |
| `-Yrepl-class-based` |
| `-Yrepl-outdir` |
| `-Yrepl-use-magic-imports` |
| `-Yscriptrunner` |
| `-Yvalidate-pos` |


## 3 - New scalacOptions in Scala 3
### 1. Standard settings

| 3.0.x | description |
|-|-|
|`-color` | Colored output Default: always. |
|`-doc-snapshot` | Generate a documentation snapshot for the current Dotty version |
|`-explain` | Explain errors in more detail. |
|`-from-tasty` | Compile classes from tasty files. The arguments are .tasty or .jar files. |
|`-indent` | Together with -rewrite, remove {...} syntax when possible due to significant indentation. |
|`-new-syntax` | Require `then` and `do` in control expressions. |
|`-noindent` | Require classical {...} syntax, indentation is not significant. |
|`-old-syntax` | Require `(...)` around conditions. |
|`-pagewidth` | Set page width Default: 80. |
|`-print-lines` | Show source code line numbers. |
|`-print-tasty` | Prints the raw tasty. |
|`-project` | The name of the project. |
|`-project-logo` | The file that contains the project's logo (in /images). |
|`-project-url` | The source repository of your project. |
|`-project-version` | The current version of your project. |
|`-rewrite` | When used in conjunction with a `...-migration` source version, rewrites sources to migrate to new version. |
|`-semanticdb-target` | Specify an alternative output directory for SemanticDB files. |
|`-siteroot` | A directory containing static files from which to generate documentation. Default: ./docs. |
|`-sourceroot` | Specify workspace root directory. Default: .. |

### 2. Compiler plugins
There are two settings in Scala 3 that replace two compiler plugins in Scala 2.13.4.
Read this [section](prerequisites.md#compiler-plugins) for more information.

| 2.13.4 | 3.0.x |
|-|-|
|`-Xplugin:semanticdb-scalac_2.13.4-4.4.0.jar`| `-Xsemanticdb` |
|`-Xplugin:kind-projector_2.13.4-0.11.2.jar` | `-Ykind-projector` |

Usually a compiler plugin is added with `addCompilerPlugin` invocation in builds 
and not as a scalacOption as in the following example.
```scala
addCompilerPlugin("org.typelevel" %% "kind-projector" % "4.4.0")
```
For those two particular compiler plugins, you will need to remove the `addCompilerPlugin`
and replace it with the corresponding scalacOption
```scala
scalacOptions ++= (if (isDotty.value) Seq("-Ykind-projector") else Seq())
```

### 3. Advanced settings

| 3.0.x | description |
|-|-|
|`-Xignore-scala2-macros` | Ignore errors when compiling code that calls Scala2 macros, these will fail at runtime. | 
|`-Ximport-suggestion-timeout` | Timeout (in ms) for searching for import suggestions when errors are reported. | 
|`-Xmax-inlined-trees` | Maximal number of inlined trees. Default: 2000000 | 
|`-Xmax-inlines` | Maximal number of successive inlines. Default: 32. | 
|`-Xprint-diff` | Print changed parts of the tree since last print. | 
|`-Xprint-diff-del` | Print changed parts of the tree since last print including deleted parts. | 
|`-Xprint-inline` | Show where inlined code comes from. | 
|`-Xprint-suspension` | Show when code is suspended until macros are compiled. | 
|`-Xrepl-disable-display` | Do not display definitions in REPL. | 
|`-Xwiki-syntax` | Retains the Scala2 behavior of using Wiki Syntax in Scaladoc. | 

### 4. Private settings

| 3.0.x | description |
|-|-|
|`-Ycheck-all-patmat` | Check exhaustivity and redundancy of all pattern matching (used for testing the algorithm). |
|`-Ycheck-mods` | Check that symbols and their defining trees have modifiers in sync. |
|`-Ycheck-reentrant` | Check that compiled program does not contain vars that can be accessed from a global root. |
|`-Ycook-comments` | Cook the comments (type check `@usecase`, etc.) |
|`-Ydebug-error` | Print the stack trace when any error is caught. |
|`-Ydebug-flags` | Print all flags of definitions. |
|`-Ydebug-missing-refs` | Print a stacktrace when a required symbol is missing. |
|`-Ydebug-names` | Show internal representation of names. |
|`-Ydebug-pos` | Show full source positions including spans. |
|`-Ydebug-trace` | Trace core operations. |
|`-Ydebug-tree-with-id` | Print the stack trace when the tree with the given id is created. Default: -2147483648. |
|`-Ydebug-type-error` | Print the stack trace when a TypeError is caught |
|`-Ydetailed-stats` | Show detailed internal compiler stats (needs Stats.enabled to be set to true). |
|`-YdisableFlatCpCaching` | Do not cache flat classpath representation of classpath elements from jars across compiler instances. |
|`-Ydrop-comments` | Drop comments when scanning source files. |
|`-Ydump-sbt-inc` | For every compiled foo.scala, output the API representation and dependencies used for sbt incremental compilation in foo.inc, implies -Yforce-sbt-phases. |
|`-Yerased-terms` | Allows the use of erased terms. |
|`-Yexplain-lowlevel` | When explaining type errors, show types at a lower level. |
|`-Yexplicit-nulls` | Make reference types non-nullable. Nullable types can be expressed with unions: e.g. String&#124;Null. |
|`-Yforce-sbt-phases` | Run the phases used by sbt for incremental compilation (ExtractDependencies and ExtractAPI) even if the compiler is ran outside of sbt, for debugging. |
|`-Yfrom-tasty-ignore-list` | List of `tasty` files in jar files that will not be loaded when using -from-tasty |
|`-Yindent-colons` | Allow colons at ends-of-lines to start indentation blocks. |
|`-Yinstrument` | Add instrumentation code that counts allocations and closure creations. |
|`-Yinstrument-defs` | Add instrumentation code that counts method calls; needs -Yinstrument to be set, too. |
|`-Yno-decode-stacktraces` | how raw StackOverflow stacktraces, instead of decoding them into triggering operations. |
|`-Yno-deep-subtypes` | Throw an exception on deep subtyping call stacks. |
|`-Yno-double-bindings` | Assert no namedtype is bound twice (should be enabled only if program is error-free). |
|`-Yno-kind-polymorphism` | Disable kind polymorphism. |
|`-Yno-patmat-opt` | Disable all pattern matching optimizations. |
|`-Yplain-printer` | Pretty-print using a plain printer. |
|`-Yprint-debug` | When printing trees, print some extra information useful for debugging. |
|`-Yprint-debug-owners` | When printing trees, print owners of definitions. |
|`-Yprint-pos` | Show tree positions. |
|`-Yprint-pos-syms` | Show symbol definitions positions. |
|`-Yprint-syms` | When printing trees print info in symbols instead of corresponding info in trees. |
|`-Yrequire-targetName` | Warn if an operator is defined without a @targetName annotation |
|`-Yretain-trees` | Retain trees for top-level classes, accessible from ClassSymbol#tree |
|`-Yscala2-unpickler` | Control where we may get Scala 2 symbols from. This is either "always", "never", or a classpath. Default: always. |
|`-Yshow-print-errors` | Don't suppress exceptions thrown during tree printing. |
|`-Yshow-suppressed-errors` | Also show follow-on errors and warnings that are normally suppressed. |
|`-Yshow-tree-ids` | Uniquely tag all tree nodes in debugging output. |
|`-Yshow-var-bounds` | Print type variables with their bounds. |
|`-Ytest-pickler` | Self-test for pickling functionality; should be used with -Ystop-after:pickler. |
|`-Yunsound-match-types` | Use unsound match type reduction algorithm. |

