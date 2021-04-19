---
id: type-checker
title: Type Checker
---

The Scala 2.13 type checker is unsound in some special cases.
This can lead to surprising runtime errors in places we would not expect.
Scala 3 is based on stronger theoretical foundations, which helped us discover and fix the unsoundness.

```scala mdoc:file:incompat-30/variance/README.md
```

```scala mdoc:file:incompat-30/pattern-match/README.md
```
