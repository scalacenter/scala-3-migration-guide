# Upgrading a Scala Application

Scala 2 is binary compatible with Scala 3. It means that you can migrate your application one module at a time. It also means that you can try those exciting new features without blocking anyone.

Prior to this you have to configure your build tool to handle several Scala versions. Sbt, Mill and other build tools can do that but not all of them. Otherwise you have to migrate the entire application all at once.

## How to?

Lunatech ["Moving from Scala 2 to Scala 3"](https://github.com/lunatech-labs/lunatech-scala-2-to-scala3-course) course is a good way to get started. It guides you through the migration of a single module Akka Typed sudoku solver project and it demonstrates various of the new features of the langugage. 

## Additional Resources

- [Dotty Getting Started](https://dotty.epfl.ch/docs/usage/getting-started.html)
- [Scala Blog Post: The Road to Scala 3](https://www.scala-lang.org/2019/12/18/road-to-scala-3.html#how-can-i-contribute)
