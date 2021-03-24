package records

final case class LargeScala[T](
  i: Int,
  s: String,
  l: Long,
  ss: SmallScala,
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
)
