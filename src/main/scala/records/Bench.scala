package records

import org.openjdk.jmh.annotations._
import java.util.concurrent._

@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Warmup(iterations = 5, time = 200, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 5, time = 200, timeUnit = TimeUnit.MILLISECONDS)
@Fork(50)
class Bench {

  val scala1 = new SmallScala(123, "hello world", 45L)
  val scala2 = new SmallScala(123, "hello world", 45L)
  val scala3 = new SmallScala(123, "hello world", 49L)

  val scala4 = new LargeScala[SmallScala](
    123, "hello world", 45L,
    scala1, scala2,
    124, "hello foo", 46L,
    125, "hello baz", 48L,
    126, "hello bar", 49L
  )
  val scala5 = new LargeScala[SmallScala](
    123, "hello world", 45L,
    scala2, scala1,
    124, "hello foo", 46L,
    125, "hello baz", 48L,
    126, "hello bar", 49L
  )
  val scala6 = new LargeScala[SmallScala](
    123, "hello world", 45L,
    scala2, scala1,
    124, "hello foo", 46L,
    125, "hello baz", 48L,
    126, "hello bar", 50L
  )

  val scala1M = new SmallScalaModified(123, "hello world", 45L)
  val scala2M = new SmallScalaModified(123, "hello world", 45L)
  val scala3M = new SmallScalaModified(123, "hello world", 49L)

  val scala4M = new LargeScalaModified[SmallScalaModified](
    123, "hello world", 45L,
    scala1M, scala2M,
    124, "hello foo", 46L,
    125, "hello baz", 48L,
    126, "hello bar", 49L
  )
  val scala5M = new LargeScalaModified[SmallScalaModified](
    123, "hello world", 45L,
    scala2M, scala1M,
    124, "hello foo", 46L,
    125, "hello baz", 48L,
    126, "hello bar", 49L
  )
  val scala6M = new LargeScalaModified[SmallScalaModified](
    123, "hello world", 45L,
    scala2M, scala1M,
    124, "hello foo", 46L,
    125, "hello baz", 48L,
    126, "hello bar", 50L
  )

  val java1 = new SmallJava(123, "hello world", 45L)
  val java2 = new SmallJava(123, "hello world", 45L)
  val java3 = new SmallJava(123, "hello world", 49L)

  val java4 = new LargeJava[SmallJava](
    123, "hello world", 45L,
    java1, java2,
    124, "hello foo", 46L,
    125, "hello baz", 48L,
    126, "hello bar", 49L
  )
  val java5 = new LargeJava[SmallJava](
    123, "hello world", 45L,
    java2, java1,
    124, "hello foo", 46L,
    125, "hello baz", 48L,
    126, "hello bar", 49L
  )
  val java6 = new LargeJava[SmallJava](
    123, "hello world", 45L,
    java2, java1,
    124, "hello foo", 46L,
    125, "hello baz", 48L,
    126, "hello bar", 50L
  )

  assert(scala1 == scala2)
  assert(scala1M == scala2M)
  assert(java1 == java2)

  assert(scala1.hashCode == scala1M.hashCode)
  assert(scala2.hashCode == scala2M.hashCode)
  assert(scala3.hashCode == scala3M.hashCode)
  assert(scala4.hashCode == scala4M.hashCode)
  assert(scala5.hashCode == scala5M.hashCode)
  assert(scala6.hashCode == scala6M.hashCode)

  // equals

  @Benchmark
  def smallScalaEqualityTest(): Boolean =
    scala1 == scala2

  @Benchmark
  def smallScalaModifiedEqualityTest(): Boolean =
    scala1M == scala2M

  @Benchmark
  def smallJavaEqualityTest(): Boolean =
    java1 == java2

  @Benchmark
  def smallScalaInequalityTest(): Boolean =
    scala1 == scala3

  @Benchmark
  def smallScalaModifiedInequalityTest(): Boolean =
    scala1M == scala3M

  @Benchmark
  def smallJavaInequalityTest(): Boolean =
    java1 == java3


  @Benchmark
  def largeScalaEqualityTest(): Boolean =
    scala4 == scala5

  @Benchmark
  def largeScalaModifiedEqualityTest(): Boolean =
    scala4M == scala5M

  @Benchmark
  def largeJavaEqualityTest(): Boolean =
    java4 == java5

  @Benchmark
  def largeScalaInequalityTest(): Boolean =
    scala4 == scala6

  @Benchmark
  def largeScalaModifiedInequalityTest(): Boolean =
    scala4M == scala6M

  @Benchmark
  def largeJavaInequalityTest(): Boolean =
    java4 == java6

  // hashCode

  @Benchmark
  def smallScalaHashCode(): Int =
    scala1.hashCode

  @Benchmark
  def smallScalaModifiedHashCode(): Int =
    scala1M.hashCode

  @Benchmark
  def smallJavaHashCode(): Int =
    java1.hashCode

  @Benchmark
  def largeScalaHashCode(): Int =
    scala4.hashCode

  @Benchmark
  def largeScalaModifiedHashCode(): Int =
    scala4M.hashCode

  @Benchmark
  def largeJavaHashCode(): Int =
    java4.hashCode

  // productElement

  @Benchmark
  def smallScalaProductElement(): Any =
    scala1.productElement(1)

  @Benchmark
  def smallScalaModifiedProductElement(): Any =
    scala1M.productElement(1)

  @Benchmark
  def largeScalaProductElement(): Any =
    scala4.productElement(8)

  @Benchmark
  def largeScalaModifiedProductElement(): Any =
    scala4M.productElement(8)
}

