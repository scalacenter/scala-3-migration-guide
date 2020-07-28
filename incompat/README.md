## Description

In this folder we track all the found incompatibilities between Scala 2 and Scala 3 (except the ones that the dotc rewrites solve).

Each incompatibility lives in its own subfolder and sbt subproject. It is described by:
- a short `README.md` detailing the origin, the compiler message, the related documentation, the related issues and PRs...
- a `src/main/scala-2.13` source directory that must compile in Scala 2.13 but not in Dotty
- a proposed solution under `src/main/scala/` that must compile with Scala 2.13 and Dotty

The sbt `<incompat>/test` task ensures both that the `scala-2.13` sources does not compile and that the `scala` sources compiles with Dotty. It will help us maintain an up-to-date view of the incompatibilities because, as Dotty progress towards the release of Scala 3, we expect some of these incompatibilites to be solved at the compiler side.

The `implicitView` incompatibility is different because it is a runtime incompatibility, meaning that the code compiles but the runtime behavior change. In this case the `implicitView/test` task checks that the `scala-2.13` runtime behavior is wrong and that the `scala` runtime behavior is correct.

The sbt `incompat` project is an aggregate of all the incompatibility projects. You can check all the incompatibilities by running `incompat/test`.

## Motivation

This set of incompatibilities will be used to track the progress of the migration guide and migration tooling. It will tell us which incompatibility lack documentation, which scalafix rule should be added, which incompatibility is lacking a generic solution.

## Index


### Dropped/Changed Features

- [abstractOverride](abstract-override/)
- [anonymousTypeParam](anonymous-type-param/)
- [any2stringaddConversion](any2stringadd-conversion/)
- [byNameParamTypeInfer](by-name-param-type-infer/)
- [defaultParamVariance](default-param-variance/)
- [earlyInitializer](early-initializer/)
- [existentialType](existential-type/)
- [explicitCallToUnapply](explicit-call-to-unapply/)
- [javaLangEnum](java-lang-enum/)
- [reflectiveCall](reflective-call/)
- [restrictedOperator](restricted-operator/)
- [typeParamIdentifier](type-param-identifier/)

### Contextual Abstractions

- [ambiguousConversion](ambiguous-conversion/)
- [implicitView](implicit-view/)
- [typeOfImplicitDef](type-of-implicit-def/)
- [viewBound](view-bound/)

### Type Inference

- [typeInfer1](type-infer-1/)
- [typeInfer2](type-infer-2/)
- [typeInfer3](type-infer-3/)
- [typeInfer4](type-infer-4/)
- [typeInfer5](type-infer-5/)
- [typeInfer6](type-infer-6/)
- [typeInfer7](type-infer-7/)
- [typeInfer8](type-infer-8/)
- [typeInfer9](type-infer-9/)
- [typeInfer10](type-infer-10/)
