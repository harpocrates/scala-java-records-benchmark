package records

import org.openjdk.jmh.infra.Blackhole
import org.openjdk.jmh.annotations._
import java.util.concurrent._

@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Warmup(iterations = 10, time = 200, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 200, timeUnit = TimeUnit.MILLISECONDS)
@Fork(50)
class BenchVariousEquals {

  val smallScala0 = new SmallScala(123, "hello world", 45L)

  def smallScala1 = new SmallScala(124, "foo bar", 98L)
  def smallScala2 = new SmallScala(123, "foo bar", 98L)
  def smallScala3 = new SmallScala(123, "hello world", 98L)
  def smallScala4 = new SmallScala(123, "hello world", 45L)

  val smallScala0M = new SmallScalaModified(123, "hello world", 45L)

  def smallScala1M = new SmallScalaModified(124, "foo bar", 98L)
  def smallScala2M = new SmallScalaModified(123, "foo bar", 98L)
  def smallScala3M = new SmallScalaModified(123, "hello world", 98L)
  def smallScala4M = new SmallScalaModified(123, "hello world", 45L)

//  @Benchmark
//  def smallScala(blackHole: Blackhole): Unit = {
//    blackHole.consume(smallScala0 == smallScala0)
//    blackHole.consume(smallScala0 == smallScala1)
//    blackHole.consume(smallScala0 == smallScala2)
//    blackHole.consume(smallScala0 == smallScala3)
//    blackHole.consume(smallScala0 == smallScala4)
//  }
//
//  @Benchmark
//  def smallScalaModified(blackHole: Blackhole): Unit = {
//    blackHole.consume(smallScala0M == smallScala0M)
//    blackHole.consume(smallScala0M == smallScala1M)
//    blackHole.consume(smallScala0M == smallScala2M)
//    blackHole.consume(smallScala0M == smallScala3M)
//    blackHole.consume(smallScala0M == smallScala4M)
//  }


  val largeScala0 = new LargeScala[SmallScala](
    123, "hello world", 45L,
    smallScala1, smallScala2,
    124, "hello foo", 46L,
    125, "hello baz", 48L,
    126, "hello bar", 49L
  )

  def largeScala1 = new LargeScala[SmallScala](
    124, "foo bar", 46L,
    smallScala2, smallScala3,
    125, "hi foo", 47L,
    124, "hi baz", 49L,
    127, "hi bar", 50L
  )
  def largeScala2 = new LargeScala[SmallScala](
    123, "foo bar", 46L,
    smallScala2, smallScala3,
    125, "hi foo", 47L,
    124, "hi baz", 49L,
    127, "hi bar", 50L
  )
  def largeScala3 = new LargeScala[SmallScala](
    123, "hello world", 46L,
    smallScala2, smallScala3,
    125, "hi foo", 47L,
    124, "hi baz", 49L,
    127, "hi bar", 50L
  )
  def largeScala4 = new LargeScala[SmallScala](
    123, "hello world", 45L,
    smallScala2, smallScala3,
    125, "hi foo", 47L,
    124, "hi baz", 49L,
    127, "hi bar", 50L
  )
  def largeScala5 = new LargeScala[SmallScala](
    123, "hello world", 45L,
    smallScala1, smallScala3,
    125, "hi foo", 47L,
    124, "hi baz", 49L,
    127, "hi bar", 50L
  )
  def largeScala6 = new LargeScala[SmallScala](
    123, "hello world", 45L,
    smallScala1, smallScala2,
    125, "hi foo", 47L,
    124, "hi baz", 49L,
    127, "hi bar", 50L
  )
  def largeScala7 = new LargeScala[SmallScala](
    123, "hello world", 45L,
    smallScala1, smallScala2,
    124, "hi foo", 47L,
    124, "hi baz", 49L,
    127, "hi bar", 50L
  )
  def largeScala8 = new LargeScala[SmallScala](
    123, "hello world", 45L,
    smallScala1, smallScala2,
    124, "hello foo", 46L,
    124, "hi baz", 49L,
    127, "hi bar", 50L
  )
  def largeScala9 = new LargeScala[SmallScala](
    123, "hello world", 45L,
    smallScala1, smallScala2,
    124, "hello foo", 46L,
    125, "hi baz", 49L,
    127, "hi bar", 50L
  )
  def largeScala10 = new LargeScala[SmallScala](
    123, "hello world", 45L,
    smallScala1, smallScala2,
    124, "hello foo", 46L,
    125, "hello baz", 49L,
    127, "hi bar", 50L
  )
  def largeScala11 = new LargeScala[SmallScala](
    123, "hello world", 45L,
    smallScala1, smallScala2,
    124, "hello foo", 46L,
    125, "hello baz", 48L,
    127, "hi bar", 50L
  )
  def largeScala12 = new LargeScala[SmallScala](
    123, "hello world", 45L,
    smallScala1, smallScala2,
    124, "hello foo", 46L,
    125, "hello baz", 48L,
    126, "hi bar", 50L
  )
  def largeScala13 = new LargeScala[SmallScala](
    123, "hello world", 45L,
    smallScala1, smallScala2,
    124, "hello foo", 46L,
    125, "hello baz", 48L,
    126, "hello bar", 50L
  )
  def largeScala14 = new LargeScala[SmallScala](
    123, "hello world", 45L,
    smallScala1, smallScala2,
    124, "hello foo", 46L,
    125, "hello baz", 48L,
    126, "hello bar", 49L
  )



  val largeScala0M = new LargeScalaModified[SmallScalaModified](
    123, "hello world", 45L,
    smallScala1M, smallScala2M,
    124, "hello foo", 46L,
    125, "hello baz", 48L,
    126, "hello bar", 49L
  )

  def largeScala1M = new LargeScalaModified[SmallScalaModified](
    124, "foo bar", 46L,
    smallScala2M, smallScala3M,
    125, "hi foo", 47L,
    124, "hi baz", 49L,
    127, "hi bar", 50L
  )
  def largeScala2M = new LargeScalaModified[SmallScalaModified](
    123, "foo bar", 46L,
    smallScala2M, smallScala3M,
    125, "hi foo", 47L,
    124, "hi baz", 49L,
    127, "hi bar", 50L
  )
  def largeScala3M = new LargeScalaModified[SmallScalaModified](
    123, "hello world", 46L,
    smallScala2M, smallScala3M,
    125, "hi foo", 47L,
    124, "hi baz", 49L,
    127, "hi bar", 50L
  )
  def largeScala4M = new LargeScalaModified[SmallScalaModified](
    123, "hello world", 45L,
    smallScala2M, smallScala3M,
    125, "hi foo", 47L,
    124, "hi baz", 49L,
    127, "hi bar", 50L
  )
  def largeScala5M = new LargeScalaModified[SmallScalaModified](
    123, "hello world", 45L,
    smallScala1M, smallScala3M,
    125, "hi foo", 47L,
    124, "hi baz", 49L,
    127, "hi bar", 50L
  )
  def largeScala6M = new LargeScalaModified[SmallScalaModified](
    123, "hello world", 45L,
    smallScala1M, smallScala2M,
    125, "hi foo", 47L,
    124, "hi baz", 49L,
    127, "hi bar", 50L
  )
  def largeScala7M = new LargeScalaModified[SmallScalaModified](
    123, "hello world", 45L,
    smallScala1M, smallScala2M,
    124, "hi foo", 47L,
    124, "hi baz", 49L,
    127, "hi bar", 50L
  )
  def largeScala8M = new LargeScalaModified[SmallScalaModified](
    123, "hello world", 45L,
    smallScala1M, smallScala2M,
    124, "hello foo", 46L,
    124, "hi baz", 49L,
    127, "hi bar", 50L
  )
  def largeScala9M = new LargeScalaModified[SmallScalaModified](
    123, "hello world", 45L,
    smallScala1M, smallScala2M,
    124, "hello foo", 46L,
    125, "hi baz", 49L,
    127, "hi bar", 50L
  )
  def largeScala10M = new LargeScalaModified[SmallScalaModified](
    123, "hello world", 45L,
    smallScala1M, smallScala2M,
    124, "hello foo", 46L,
    125, "hello baz", 49L,
    127, "hi bar", 50L
  )
  def largeScala11M = new LargeScalaModified[SmallScalaModified](
    123, "hello world", 45L,
    smallScala1M, smallScala2M,
    124, "hello foo", 46L,
    125, "hello baz", 48L,
    127, "hi bar", 50L
  )
  def largeScala12M = new LargeScalaModified[SmallScalaModified](
    123, "hello world", 45L,
    smallScala1M, smallScala2M,
    124, "hello foo", 46L,
    125, "hello baz", 48L,
    126, "hi bar", 50L
  )
  def largeScala13M = new LargeScalaModified[SmallScalaModified](
    123, "hello world", 45L,
    smallScala1M, smallScala2M,
    124, "hello foo", 46L,
    125, "hello baz", 48L,
    126, "hello bar", 50L
  )
  def largeScala14M = new LargeScalaModified[SmallScalaModified](
    123, "hello world", 45L,
    smallScala1M, smallScala2M,
    124, "hello foo", 46L,
    125, "hello baz", 48L,
    126, "hello bar", 49L
  )


  /*
  @Benchmark def largeScalaBench0 = largeScala0 == largeScala0
  @Benchmark def largeScalaBench1 = largeScala0 == largeScala1
  @Benchmark def largeScalaBench2 = largeScala0 == largeScala2
  @Benchmark def largeScalaBench3 = largeScala0 == largeScala3
  @Benchmark def largeScalaBench4 = largeScala0 == largeScala4
  @Benchmark def largeScalaBench5 = largeScala0 == largeScala5
  @Benchmark def largeScalaBench6 = largeScala0 == largeScala6
  @Benchmark def largeScalaBench7 = largeScala0 == largeScala7
  @Benchmark def largeScalaBench8 = largeScala0 == largeScala8
  @Benchmark def largeScalaBench9 = largeScala0 == largeScala9
  @Benchmark def largeScalaBench10 = largeScala0 == largeScala10
  @Benchmark def largeScalaBench11 = largeScala0 == largeScala11
  @Benchmark def largeScalaBench12 = largeScala0 == largeScala13
  @Benchmark def largeScalaBench13 = largeScala0 == largeScala13
  @Benchmark def largeScalaBench14 = largeScala0 == largeScala14

  @Benchmark def largeScalaModBench0 = largeScala0M == largeScala0M
  @Benchmark def largeScalaModBench1 = largeScala0M == largeScala1M
  @Benchmark def largeScalaModBench2 = largeScala0M == largeScala2M
  @Benchmark def largeScalaModBench3 = largeScala0M == largeScala3M
  @Benchmark def largeScalaModBench4 = largeScala0M == largeScala4M
  @Benchmark def largeScalaModBench5 = largeScala0M == largeScala5M
  @Benchmark def largeScalaModBench6 = largeScala0M == largeScala6M
  @Benchmark def largeScalaModBench7 = largeScala0M == largeScala7M
  @Benchmark def largeScalaModBench8 = largeScala0M == largeScala8M
  @Benchmark def largeScalaModBench9 = largeScala0M == largeScala9M
  @Benchmark def largeScalaModBench10 = largeScala0M == largeScala10M
  @Benchmark def largeScalaModBench11 = largeScala0M == largeScala11M
  @Benchmark def largeScalaModBench12 = largeScala0M == largeScala13M
  @Benchmark def largeScalaModBench13 = largeScala0M == largeScala13M
  @Benchmark def largeScalaModBench14 = largeScala0M == largeScala14M
*/

  @Benchmark
  def largeScala(blackHole: Blackhole): Unit = {
    blackHole.consume(largeScala0 == largeScala0)
    blackHole.consume(largeScala0 == largeScala1)
    blackHole.consume(largeScala0 == largeScala2)
    blackHole.consume(largeScala0 == largeScala3)
    blackHole.consume(largeScala0 == largeScala4)
    blackHole.consume(largeScala0 == largeScala5)
    blackHole.consume(largeScala0 == largeScala6)
    blackHole.consume(largeScala0 == largeScala7)
    blackHole.consume(largeScala0 == largeScala8)
    blackHole.consume(largeScala0 == largeScala9)
    blackHole.consume(largeScala0 == largeScala10)
    blackHole.consume(largeScala0 == largeScala11)
    blackHole.consume(largeScala0 == largeScala12)
    blackHole.consume(largeScala0 == largeScala13)
    blackHole.consume(largeScala0 == largeScala14)
  }

  @Benchmark
  def largeScalaModified(blackHole: Blackhole): Unit = {
    blackHole.consume(largeScala0M == largeScala0M)
    blackHole.consume(largeScala0M == largeScala1M)
    blackHole.consume(largeScala0M == largeScala2M)
    blackHole.consume(largeScala0M == largeScala3M)
    blackHole.consume(largeScala0M == largeScala4M)
    blackHole.consume(largeScala0M == largeScala5M)
    blackHole.consume(largeScala0M == largeScala6M)
    blackHole.consume(largeScala0M == largeScala7M)
    blackHole.consume(largeScala0M == largeScala8M)
    blackHole.consume(largeScala0M == largeScala9M)
    blackHole.consume(largeScala0M == largeScala10M)
    blackHole.consume(largeScala0M == largeScala11M)
    blackHole.consume(largeScala0M == largeScala12M)
    blackHole.consume(largeScala0M == largeScala13M)
    blackHole.consume(largeScala0M == largeScala14M)
  }
}
