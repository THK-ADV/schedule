package collision

sealed trait CollisionType {
  def label: String
  override def toString = label
}

object CollisionType {
  case object Room extends CollisionType {
    override val label = "Parallele Raumnutzung"
  }
  case object StudyPathTime extends CollisionType {
    override val label = "Kursüberschneidung im Studiengang"
  }
  case object BlockedDay extends CollisionType {
    override val label = "Geblockter Tag"
  }
}
