## Type Of Implicit Definition

Type of implicit definitions (`val` or `def`) are now required by the Scala 3.0 compiler.
They cannot be inferred.

Wherever the type annotation of an implicit definition is missing, the Scala 3.0 compiler will print an error message of the form:

```
-- Error: src/main/scala/type-of-implicit-def.scala:4:15 
4 |  implicit val context = new Context {}
  |               ^
  |               type of implicit definition needs to be given explicitly
```

#### Scalafix rewrite

The Scalafix rule named `ExplicitImplicitTypes` in [ohze/scala-rewrites](https://github.com/ohze/scala-rewrites#fixexplicittypesexplicitimplicittypes) repository can write the missing type annotations automatically.
