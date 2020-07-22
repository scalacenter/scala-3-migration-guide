Dotty implementation of pattern matching has been simplified ([see reference](https://dotty.epfl.ch/docs/reference/changed-features/pattern-matching.html)). As a consequence the generated `unapply` methods are now option-less which makes explicit calls to `unapply` incompatible between Scala 2 and Scala 3.

The proposed solution to this incompatibility is to use extraction instead of an explicit call to `unapply`.

[Related issue](https://github.com/lampepfl/dotty/issues/2335)

## Error message

As of `0.25.0-RC2` the error message is:

```
[error] -- [E008] Not Found Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/explicit-call-to-unapply/src/main/scala-2.13/explicit-call-to-unapply.scala:5:21 
[error] 5 |    Foo.unapply(foo).get
[error]   |    ^^^^^^^^^^^^^^^^^^^^
[error]   |    value get is not a member of Foo
```
