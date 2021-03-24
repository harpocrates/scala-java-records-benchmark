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

  @Benchmark
  def smallScalaEqualityTest(): Boolean =
    scala1 == scala2

  @Benchmark
  def smallJavaEqualityTest(): Boolean =
    java1 == java2

  @Benchmark
  def smallScalaInequalityTest(): Boolean =
    scala1 == scala3

  @Benchmark
  def smallJavaInequalityTest(): Boolean =
    java1 == java3


  @Benchmark
  def largeScalaEqualityTest(): Boolean =
    scala4 == scala5

  @Benchmark
  def largeJavaEqualityTest(): Boolean =
    java4 == java5

  @Benchmark
  def largeScalaInequalityTest(): Boolean =
    scala4 == scala6

  @Benchmark
  def largeJavaInequalityTest(): Boolean =
    java4 == java6
}

