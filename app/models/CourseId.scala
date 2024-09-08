package models

import play.api.libs.json.Writes

enum CourseId(val id: String):
  override def toString = id

  case Lecture extends CourseId("lecture")
  case Seminar extends CourseId("seminar")
  case Practical extends CourseId("practical")
  case Exercise extends CourseId("exercise")
  case Tutorial extends CourseId("tutorial")

object CourseId:
  def apply(id: String): CourseId =
    id match {
      case "lecture"   => Lecture
      case "seminar"   => Seminar
      case "practical" => Practical
      case "exercise"  => Exercise
      case "tutorial"  => Tutorial
    }

  given Writes[CourseId] =
    Writes.of[String].contramap(_.id)
