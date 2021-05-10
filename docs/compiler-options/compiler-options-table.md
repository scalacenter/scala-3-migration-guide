---
id: compiler-options-table
title: Compiler Options Table
---

When porting a Scala 2.13 project to Scala 3, you must adapt the list of compiler options.
Indeed many options have been renamed and many others are not yet available in Scala 3.

The current page contains the look-up tables to help you translate a pre-existing list of Scala 2.13 options into Scala 3.
The next page is dedicated to the new Scala 3 options.

> Passing a unavailable option to the Scala 3 compiler does not make it fail.
> It just prints a warning and ignores the option.

The compiler options are classified and ordered according to their Scala 2.13 name. Each Scala 2.13 option is associated with its Scala 3 counterpart:

| 3.0.x | Meaning |
|-|-|
| <i class="fas fa-check fa-lg"></i> | it is avalaible in Scala 3 |
| `<new-name>` | It has been renamed to `<new-name>` |
| <i class="fas fa-times fa-lg"></i> | It is not available in 3.0.0 but it could be added later |

> The comparison is based on Scala 2.13.4 and 3.0.0-M3. 

## Standard Settings

| 2.13.x | 3.0.x |
|-|-|
| `-Dproperty=value` | <i class="fas fa-times fa-lg"></i> |
| `-J<flag>` | <i class="fas fa-times fa-lg"></i> |
| `-P:<plugin>:<opt>` |<i class="fas fa-check fa-lg"></i>|
| `-V` | <i class="fas fa-times fa-lg"></i> |
| `-W` | <i class="fas fa-times fa-lg"></i> |
| `-X` |<i class="fas fa-check fa-lg"></i>|
| `-Y` |<i class="fas fa-check fa-lg"></i>|
| `-bootclasspath` |<i class="fas fa-check fa-lg"></i>| 
| `-classpath` |<i class="fas fa-check fa-lg"></i>| 
| `-d` |<i class="fas fa-check fa-lg"></i>|
| `-dependencyfile` | <i class="fas fa-times fa-lg"></i> |
| `-deprecation` |<i class="fas fa-check fa-lg"></i>| 
| `-encoding` |<i class="fas fa-check fa-lg"></i>| 
| `-explaintypes` | `-explain-types` | 
| `-extdirs` |<i class="fas fa-check fa-lg"></i>| 
| `-feature` |<i class="fas fa-check fa-lg"></i>|
| `-g` | <i class="fas fa-times fa-lg"></i> |
| `-help` |<i class="fas fa-check fa-lg"></i>| 
| `-javabootclasspath` |<i class="fas fa-check fa-lg"></i>| 
| `-javaextdirs` |<i class="fas fa-check fa-lg"></i>| 
| `-language` |<i class="fas fa-check fa-lg"></i>|
| `-no-specialization` | <i class="fas fa-times fa-lg"></i> |
| `-nobootcp` | <i class="fas fa-times fa-lg"></i> |
| `-nowarn` |<i class="fas fa-check fa-lg"></i>|
| `-opt` | <i class="fas fa-times fa-lg"></i> |
| `-opt-inline-from` | <i class="fas fa-times fa-lg"></i> |
| `-opt-warnings` | <i class="fas fa-times fa-lg"></i> |
| `-optimize` | <i class="fas fa-times fa-lg"></i> |
| `-print` |<i class="fas fa-check fa-lg"></i>| 
| `-release` |<i class="fas fa-check fa-lg"></i>|
| `-rootdir` | <i class="fas fa-times fa-lg"></i> |
| `-sourcepath` |<i class="fas fa-check fa-lg"></i>| 
| `-target` | `-Xtarget` |
| `-toolcp` | <i class="fas fa-times fa-lg"></i> |
| `-unchecked` |<i class="fas fa-check fa-lg"></i>| 
| `-uniqid` |<i class="fas fa-check fa-lg"></i>| 
| `-usejavacp` |<i class="fas fa-check fa-lg"></i>|
| `-usemanifestc` | <i class="fas fa-times fa-lg"></i> |
| `-verbose` |<i class="fas fa-check fa-lg"></i>| 
| `-version` |<i class="fas fa-check fa-lg"></i>|

## Advanced Settings

| 2.13.x | 3.0.x |
|-|-|
| `-X` |<i class="fas fa-check fa-lg"></i>|
| `-Xcheckinit` | `-Ycheck-init` |
| `-Xdev` | <i class="fas fa-times fa-lg"></i> |
| `-Xdisable-assertions` | <i class="fas fa-times fa-lg"></i> |
| `-Xelide-below` | <i class="fas fa-times fa-lg"></i> |
| `-Xexperimental` | <i class="fas fa-times fa-lg"></i> |
| `-Xfuture` | <i class="fas fa-times fa-lg"></i> |
| `-Xgenerate-phase-graph` | <i class="fas fa-times fa-lg"></i> |
| `-Xjline` | <i class="fas fa-times fa-lg"></i> |
| `-Xlint:deprecation` | `-deprecation` |
| `-Xlint:<warnings>` | <i class="fas fa-times fa-lg"></i> |
| `-Xlog-implicit-conversion` | <i class="fas fa-times fa-lg"></i> |
| `-Xlog-implicits` | <i class="fas fa-times fa-lg"></i> |
| `-Xlog-reflective-calls` | <i class="fas fa-times fa-lg"></i> |
| `-Xmacro-settings` | <i class="fas fa-times fa-lg"></i> |
| `-Xmain-class` | <i class="fas fa-times fa-lg"></i> |
| `-Xmaxerrs` | <i class="fas fa-times fa-lg"></i> |
| `-Xmaxwarns` | <i class="fas fa-times fa-lg"></i> |
| `-Xmigration` |<i class="fas fa-check fa-lg"></i>| 
| `-Xmixin-force-forwarders` |<i class="fas fa-check fa-lg"></i>| 
| `-Xno-forwarders` |<i class="fas fa-check fa-lg"></i>|
| `-Xno-patmat-analysis` | <i class="fas fa-times fa-lg"></i> |
| `-Xnojline` | <i class="fas fa-times fa-lg"></i> |
| `-Xplugin` |<i class="fas fa-check fa-lg"></i>| 
| `-Xplugin-disable` |<i class="fas fa-check fa-lg"></i>| 
| `-Xplugin-list` |<i class="fas fa-check fa-lg"></i>| 
| `-Xplugin-require` |<i class="fas fa-check fa-lg"></i>| 
| `-Xpluginsdir` |<i class="fas fa-check fa-lg"></i>|
| `-Xprint-args` | <i class="fas fa-times fa-lg"></i> |
| `-Xprompt` |<i class="fas fa-check fa-lg"></i>|
| `-Xreporter` | <i class="fas fa-times fa-lg"></i> |
| `-Xresident` | <i class="fas fa-times fa-lg"></i> |
| `-Xscript` | <i class="fas fa-times fa-lg"></i> |
| `-Xshow-class <class>` | <i class="fas fa-times fa-lg"></i> |
| `-Xshow-object <object>` | <i class="fas fa-times fa-lg"></i> |
| `-Xsource` | `-source` |
| `-Xsource-reader` | <i class="fas fa-times fa-lg"></i> |
| `-Xverify` | `-Xverify-signatures` |
| `-Xxml` | <i class="fas fa-times fa-lg"></i> |

## Private settings

| 2.13.x | 3.0.x |
|-|-|
| `-Ybackend-parallelism` | <i class="fas fa-times fa-lg"></i> |
| `-Ybackend-worker-queue` | <i class="fas fa-times fa-lg"></i> |
| `-Ybreak-cycles` | <i class="fas fa-times fa-lg"></i> |
| `-Ycache-macro-class-loader` | <i class="fas fa-times fa-lg"></i> |
| `-Ycache-plugin-class-loader` | <i class="fas fa-times fa-lg"></i> |
| `-Ycheck` |<i class="fas fa-check fa-lg"></i>|
| `-Ycompact-trees` | <i class="fas fa-times fa-lg"></i> |
| `-Ydelambdafy` | <i class="fas fa-times fa-lg"></i> |
| `-Ydump-classes` |<i class="fas fa-check fa-lg"></i>|
| `-Ygen-asmp` | <i class="fas fa-times fa-lg"></i> |
| `-Yhot-statistics` | <i class="fas fa-times fa-lg"></i> |
| `-Yide-debug` | <i class="fas fa-times fa-lg"></i> |
| `-Yimports` | <i class="fas fa-times fa-lg"></i> |
| `-Yissue-debug` | <i class="fas fa-times fa-lg"></i> |
| `-Yjar-compression-level` | <i class="fas fa-times fa-lg"></i> |
| `-YjarFactory` | <i class="fas fa-times fa-lg"></i> |
| `-Ymacro-debug-lite` | <i class="fas fa-times fa-lg"></i> |
| `-Ymacro-debug-verbose` | <i class="fas fa-times fa-lg"></i> |
| `-Ymacro-annotations` | <i class="fas fa-times fa-lg"></i> |
| `-Ymacro-classpath` | <i class="fas fa-times fa-lg"></i> |
| `-Ymacro-expand` | <i class="fas fa-times fa-lg"></i> |
| `-Ymacro-global-fresh-names` | <i class="fas fa-times fa-lg"></i> |
| `-Yno-completion` | <i class="fas fa-times fa-lg"></i> |
| `-Yno-flat-classpath-cache` | <i class="fas fa-times fa-lg"></i> |
| `-Yno-generic-signatures` |<i class="fas fa-check fa-lg"></i>| 
| `-Yno-imports` |<i class="fas fa-check fa-lg"></i>|
| `-Yno-predef` |<i class="fas fa-check fa-lg"></i>|
| `-Yopt-inline-heuristics` | <i class="fas fa-times fa-lg"></i> |
| `-Yopt-log-inline <package/Class.method>` | <i class="fas fa-times fa-lg"></i> |
| `-Yopt-trace <package/Class.method>` | <i class="fas fa-times fa-lg"></i> |
| `-Ypatmat-debug` | <i class="fas fa-times fa-lg"></i> |
| `-Ypatmat-exhaust-depth` | <i class="fas fa-times fa-lg"></i> |
| `-Ypos-debug` | <i class="fas fa-times fa-lg"></i> |
| `-Ypresentation-any-thread` | <i class="fas fa-times fa-lg"></i> |
| `-Ypresentation-debug` | <i class="fas fa-times fa-lg"></i> |
| `-Ypresentation-delay` | <i class="fas fa-times fa-lg"></i> |
| `-Ypresentation-locate-source-file` | <i class="fas fa-times fa-lg"></i> |
| `-Ypresentation-log` | <i class="fas fa-times fa-lg"></i> |
| `-Ypresentation-strict` | <i class="fas fa-times fa-lg"></i> |
| `-Ypresentation-verbose` | <i class="fas fa-times fa-lg"></i> |
| `-Yprint-trees` | <i class="fas fa-times fa-lg"></i> |
| `-Yprofile-destination` |<i class="fas fa-check fa-lg"></i>| 
| `-Yprofile-enabled` |<i class="fas fa-check fa-lg"></i>|
| `-Yprofile-trace` | <i class="fas fa-times fa-lg"></i> |
| `-Yquasiquote-debug` | <i class="fas fa-times fa-lg"></i> |
| `-Yrangepos` | <i class="fas fa-times fa-lg"></i> |
| `-Yrecursion` | <i class="fas fa-times fa-lg"></i> |
| `-Yreify-copypaste` | <i class="fas fa-times fa-lg"></i> |
| `-Yreify-debug` | <i class="fas fa-times fa-lg"></i> |
| `-Yrepl-class-based` | <i class="fas fa-times fa-lg"></i> |
| `-Yrepl-outdir` | <i class="fas fa-times fa-lg"></i> |
| `-Yrepl-use-magic-imports` | <i class="fas fa-times fa-lg"></i> |
| `-Yresolve-term-conflict` |<i class="fas fa-check fa-lg"></i>|
| `-Yscriptrunner` | <i class="fas fa-times fa-lg"></i> |
| `-Yskip` |<i class="fas fa-check fa-lg"></i>| 
| `-Yshow:<phases>` | <i class="fas fa-times fa-lg"></i> |
| `-Yshow-member-pos <output style>` | <i class="fas fa-times fa-lg"></i> |
| `-Yshow-symkinds` | <i class="fas fa-times fa-lg"></i> |
| `-Yshow-symowners` | <i class="fas fa-times fa-lg"></i> |
| `-Yshow-syms` | <i class="fas fa-times fa-lg"></i> |
| `-Ystatistics <phases>` | <i class="fas fa-times fa-lg"></i> |
| `-Ystop-after` |<i class="fas fa-check fa-lg"></i>| 
| `-Ystop-before` |<i class="fas fa-check fa-lg"></i>| 
| `-Ytyper-debug` | <i class="fas fa-times fa-lg"></i> |
| `-Yvalidate-pos` | <i class="fas fa-times fa-lg"></i> |
| `-Ywarn-dead-code` | <i class="fas fa-times fa-lg"></i> |
| `-Ywarn-numeric-widen` | <i class="fas fa-times fa-lg"></i> |
| `-Ywarn-unused:<warnings>` | <i class="fas fa-times fa-lg"></i> |
| `-Ywarn-value-discard` | <i class="fas fa-times fa-lg"></i> |

## Verbose Settings

Verbose settings were introduced in 2.13.
Most of them are not yet implemented in Scala 3.

| 2.13.x | 3.0.x |
|-|-|
| `-Vbrowse:<phases>` | <i class="fas fa-times fa-lg"></i> |
| `-Vdebug-tasty` | <i class="fas fa-times fa-lg"></i> |
| `-Vdoc` | <i class="fas fa-times fa-lg"></i> |
| `-Vfree-terms` | <i class="fas fa-times fa-lg"></i> |
| `-Vfree-types` | <i class="fas fa-times fa-lg"></i> |
| `-Vhot-statistics`| <i class="fas fa-times fa-lg"></i> |
| `-Vide`| <i class="fas fa-times fa-lg"></i> |
| `-Vimplicit-conversions`| <i class="fas fa-times fa-lg"></i> |
| `-Vimplicits`| <i class="fas fa-times fa-lg"></i> |
| `-Vinline <package/Class.method>` | <i class="fas fa-times fa-lg"></i> |
| `-Vissue`| <i class="fas fa-times fa-lg"></i> |
| `-Vmacro` | <i class="fas fa-times fa-lg"></i> |
| `-Vmacro-lite` | <i class="fas fa-times fa-lg"></i> |
| `-Vopt <package/Class.method>` | <i class="fas fa-times fa-lg"></i> |
| `-Vpatmat` | <i class="fas fa-times fa-lg"></i> |
| `-Vpos`| <i class="fas fa-times fa-lg"></i> |
| `-Vprint:<phases>` | `-Xprint:<phases>` |
| `-Vphases` | `-Xshow-phases` |
| `-Vclasspath` | `-Ylog-classpath` |
| `-Vlog:<phases>` | `-Ylog:<phases>`|
| `-Vdebug` | `-Ydebug` |
| `-Vprint-args <file>` | <i class="fas fa-times fa-lg"></i> |
| `-Vprint-pos` | `-Yprint-pos` |
| `-Vprint-types` | `-Xprint-types` |
| `-Vquasiquote` | <i class="fas fa-times fa-lg"></i> |
| `-Vreflective-calls` | <i class="fas fa-times fa-lg"></i> |
| `-Vreify` | <i class="fas fa-times fa-lg"></i> |
| `-Vshow:<phases>` | <i class="fas fa-times fa-lg"></i> |
| `-Vshow-class <class>` | <i class="fas fa-times fa-lg"></i> |
| `-Vshow-member-pos <output style>` | <i class="fas fa-times fa-lg"></i> |
| `-Vshow-object <object>` | <i class="fas fa-times fa-lg"></i> |
| `-Vshow-symkinds` | <i class="fas fa-times fa-lg"></i> |
| `-Vshow-symowners` | <i class="fas fa-times fa-lg"></i> |
| `-Vstatistics <phases>` | <i class="fas fa-times fa-lg"></i> |
| `-Vsymbols` | <i class="fas fa-times fa-lg"></i> |
| `-Vtyper` | <i class="fas fa-times fa-lg"></i> |

## Warning Settings

Warning settings were introduced in 2.13.
Most of them are not yet implemented in Scala 3.

| 2.13.x | 3.0.x |
|-|-|
| `-Wconf` | <i class="fas fa-times fa-lg"></i> |
| `-Wdead-code` | <i class="fas fa-times fa-lg"></i> |
| `-Werror` | `-Xfatal-warnings` |
| `-Wextra-implicit` | <i class="fas fa-times fa-lg"></i> |
| `-Wmacros:<mode>` | <i class="fas fa-times fa-lg"></i> |
| `-Wnumeric-widen` | <i class="fas fa-times fa-lg"></i> |
| `-Woctal-literal` | <i class="fas fa-times fa-lg"></i> |
| `-Wunused:<warnings>` | <i class="fas fa-times fa-lg"></i> |
| `-Wvalue-discard`| <i class="fas fa-times fa-lg"></i> |
| `-Wself-implicit` | <i class="fas fa-times fa-lg"></i> |

## Compiler Plugins

Some useful Scala 2.13 compiler plugins are now shipped into the compiler.
You can enable and configure them with some new native options.

### Scala.js

| 2.13.x | 3.0.x |
|-|-|
| `-Xplugin:scalajs-compiler_<version>.jar` | `-scalajs` |
| `-P:scalajs:genStaticForwardersForNonTopLevelObjects` | `-scalajs-genStaticForwardersForNonTopLevelObjects` |
| `-P:scalajs:mapSourceURI`| `-scalajs-mapSourceURI`|

### SemanticDB

| 2.13.x | 3.0.x |
|-|-|
| `-Xplugin:semanticdb-scalac_<version>.jar`| `-Xsemanticdb` |
| `-P:semanticdb:targetroot:<path>` | `-semanticdb-target:<path>` |

### Kind-Projector

| 2.13.x | 3.0.x |
|-|-|
| `-Xplugin:kind-projector_<version>.jar` | `-Ykind-projector` |
| `-P:kind-projector:underscore-placeholders` | `-Ykind-projector:underscores` |
