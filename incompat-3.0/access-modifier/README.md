## Non-private Constructor In Private Class

This incompatibility is inspired by [this commit](https://github.com/ekrich/sconfig/commit/76f2ea0b3d4beb20887a0ee63e9f151303843f9e#diff-f0d2b577a86f18363e5c552ea0f4ab63) in [sconfig](https://github.com/ekrich/sconfig). 

```scala
package foo

private class Bar private[foo] () {}
```

The error message is:
```
[error] -- Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/access-modifier/src/main/scala-2.13/access-modifier.scala:4:19 
[error] 4 |  private class Bar private[foo] ()
[error]   |                   ^
[error]   |      non-private constructor Bar in class Bar refers to private class Bar
[error]   |      in its type signature (): foo.Foo.Bar
```

The `-source:3.0-migration` mode would compile with a warning but no rewrite is provided.

The solution is to make the constructor private, since the class is private.
