package models

sealed trait Person extends UniqueEntity[String] {
  def id: String
  def kind: String
}

object Person {
  val DefaultKind = "default"
  val GroupKind = "group"
  val UnknownKind = "unknown"

  case class Default(
      id: String,
      lastname: String,
      firstname: String,
      title: Option[String],
      abbreviation: String,
      campusId: String,
      active: Boolean
  ) extends Person {
    override val kind = DefaultKind
  }

  case class Group(id: String, label: String) extends Person {
    override val kind = GroupKind
  }

  case class Unknown(id: String, label: String) extends Person {
    override val kind = UnknownKind
  }
}
