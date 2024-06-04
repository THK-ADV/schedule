package models

import play.api.libs.json.Format

sealed trait SemesterPlanEntryType {
  def id: String
}

object SemesterPlanEntryType {
  def format: Format[SemesterPlanEntryType] =
    Format.of[String].bimap(apply, _.id)

  object Exam extends SemesterPlanEntryType {
    override def id: String = "exam"
  }

  object Lecture extends SemesterPlanEntryType {
    override def id: String = "lecture"
  }

  object Block extends SemesterPlanEntryType {
    override def id: String = "block"
  }

  object Project extends SemesterPlanEntryType {
    override def id: String = "project"
  }

  object ClosedBuilding extends SemesterPlanEntryType {
    override def id: String = "closed_building"
  }

  def apply(id: String) =
    id match {
      case "exam"            => Exam
      case "lecture"         => Lecture
      case "block"           => Block
      case "project"         => Project
      case "closed_building" => ClosedBuilding
    }
}
