package models

sealed trait ModulePart {
  def value: String
  override def toString = value
}

object ModulePart {
  case object Lecture extends ModulePart {
    override val value: String = "lecture"
  }
  case object Seminar extends ModulePart {
    override val value: String = "seminar"
  }
  case object Practical extends ModulePart {
    override val value: String = "practical"
  }
  case object Exercise extends ModulePart {
    override val value: String = "exercise"
  }
  case object Tutorial extends ModulePart {
    override val value: String = "tutorial"
  }

  def apply(value: String): ModulePart = value match {
    case "lecture"   => Lecture
    case "seminar"   => Seminar
    case "practical" => Practical
    case "exercise"  => Exercise
    case "tutorial"  => Tutorial
  }
}
