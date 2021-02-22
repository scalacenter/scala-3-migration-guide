## Unsoundness fixes in pattern matching

Scala 3 fixes some unsoundness bugs in pattern matching, preventing some semantically wrong match expressions to type check.

For instance, the match expression in `combineReq` is compiling in Scala 2 but not in Scala 3.

```scala
trait Request
case class Fetch[A](ids: Set[A]) extends Request

object Request {
  def combineFetch[A](x: Fetch[A], y: Fetch[A]): Fetch[A] = Fetch(x.ids ++ y.ids)

  def combineReq(x: Request, y: Request): Request = {
    (x, y) match {
      case (x @ Fetch(_), y @ Fetch(_)) => combineFetch(x, y)
    }
  }
}
```

The error message is:

```shell
[error] -- [E007] Type Mismatch Error: src/main/scala/pattern-match.scala:9:59 
[error] 9 |      case (x @ Fetch(_), y @ Fetch(_)) => combineFetch(x, y)
[error]   |                                                           ^
[error]   |                                                Found:    (y : Fetch[A$2])
[error]   |                                                Required: Fetch[A$1]
```

Which is right, there is no proof that `x` and `y` have the same type paramater `A`.

Coming from Scala 2, this is clearly an improvement to help us locate mistakes in our code.
To solve this incompatibility it is better to find a solution that can be checked by the compiler.
It is not always easy and sometimes it is even not possible, in which case the code  is likely to fail at runtime.

In this example, a type-checked solution is to specify that the type parameter `A` in `combineFetch` is not the precise type parameter of `x` and `y` but a common ancestor.

```scala
def combineFetch[A](x: Fetch[_ <: A], y: Fetch[_ <: A]): Fetch[A] = Fetch(x.ids ++ y.ids)
```

Alternatively, a general but unsafe solution is to cast `x` and `y`.
