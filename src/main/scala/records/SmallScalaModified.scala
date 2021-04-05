package records

import java.lang.invoke._

final case class SmallScalaModified(
  i: Int,
  s: String,
  l: Long
) {
  override def equals(other: Any): Boolean = Macros.equals(this, other)
  override def hashCode: Int = Macros.hashCode(this)
  override def productElement(i: Int): AnyRef = Macros.productElement(this, i)
}
