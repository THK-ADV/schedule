package models

import localization.LocalizedLabel
import play.api.libs.json.Writes

sealed trait ModulePart extends LocalizedLabel {
  def id: String
  override def toString = id
}

object ModulePart {
  implicit def writes: Writes[ModulePart] =
    Writes.of[String].contramap(_.id)

  case object Lecture extends ModulePart {
    override def id: String = "lecture"
    override def deLabel: String = "Vorlesung"
    override def enLabel: String = "Lecture"
  }
  case object Seminar extends ModulePart {
    override def id: String = "seminar"
    override def deLabel: String = "Seminar"
    override def enLabel: String = "Seminar"
  }
  case object Practical extends ModulePart {
    override def id: String = "practical"
    override def deLabel: String = "Praktikum"
    override def enLabel: String = "Lab"
  }
  case object Exercise extends ModulePart {
    override def id: String = "exercise"
    override def deLabel: String = "Übung"
    override def enLabel: String = "Exercise"
  }
  case object Tutorial extends ModulePart {
    override def id: String = "tutorial"
    override def deLabel: String = "Tutorium"
    override def enLabel: String = "Tutorial"
  }

  def apply(id: String): ModulePart =
    id match {
      case "lecture"   => Lecture
      case "seminar"   => Seminar
      case "practical" => Practical
      case "exercise"  => Exercise
      case "tutorial"  => Tutorial
    }
}
