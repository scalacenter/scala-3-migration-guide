Type of implicit definitions (`val` or `def`) are now required by the Dotty compiler.

As of `0.25.0-RC2` the error message is:
```
[error] -- Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/type-of-implicit-def/src/main/scala-2.13/type-annot.scala:4:15 
[error] 4 |  implicit val context = new Context {}
[error]   |               ^
[error]   |               type of implicit definition needs to be given explicitly
```
