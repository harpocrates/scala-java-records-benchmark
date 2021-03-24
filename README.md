Benchmark to compare `equals` on Java records to Scala case classes.

  * Java uses `invokedynamic` with `java.lang.runtime.ObjectMethods`
  * Scala generates bytecode directly in `equals`

With JDK 16, `sbt jmh:run`:

```
...
[info] Bench.largeJavaEqualityTest     avgt  250   9.689 ± 0.140  ns/op
[info] Bench.largeJavaInequalityTest   avgt  250   2.949 ± 0.111  ns/op
[info] Bench.largeScalaEqualityTest    avgt  250  31.844 ± 0.352  ns/op
[info] Bench.largeScalaInequalityTest  avgt  250   7.203 ± 0.065  ns/op
[info] Bench.smallJavaEqualityTest     avgt  250   3.856 ± 0.043  ns/op
[info] Bench.smallJavaInequalityTest   avgt  250   3.234 ± 0.219  ns/op
[info] Bench.smallScalaEqualityTest    avgt  250   7.307 ± 0.112  ns/op
[info] Bench.smallScalaInequalityTest  avgt  250   3.473 ± 0.030  ns/op
```

Conclusion: __Java wins in every dimension__

  * it is faster

  * the bytecode for `equals` is much shorter (especially when you account for
    the fact the bootstrap method is re-used for `hashCode` and `toString`)

  * the implementation of `ObjectMethods` is flexible, so can be improved in
    future JVMs without the bytecode `invokedynamic` call needing to change.

