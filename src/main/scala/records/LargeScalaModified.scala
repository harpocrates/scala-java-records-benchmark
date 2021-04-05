package records

class Foo {
  def bar() = 89
}


final case class LargeScalaModified[T](
  i: Int,
  s: String,
  l: Long,
  ss: SmallScalaModified,
  t: T,
  i2: Int,
  s2: String,
  l2: Long,
  i3: Int,
  s3: String,
  l3: Long,
  i4: Int,
  s4: String,
  l4: Long
) extends Foo {
  override def equals(other: Any): Boolean = Macros.equals(this, other)
  override def hashCode: Int = Macros.hashCode(this)
  override def productElement(i: Int): AnyRef = Macros.productElement(this, i)
}
