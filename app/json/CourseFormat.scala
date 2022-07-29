package json

import models.Course.{CourseAtom, CourseDefault}
import models.{Course, CourseJson}
import play.api.libs.json.{Json, OFormat, Writes}

trait CourseFormat {
  self: CourseIntervalFormat
    with CourseTypeFormat
    with SubmoduleFormat
    with UserFormat
    with SemesterFormat =>
  implicit val courseJsonFmt: OFormat[CourseJson] = Json.format[CourseJson]

  implicit val courseWrites: Writes[Course] = Writes.apply {
    case default: CourseDefault => courseDefaultWrites.writes(default)
    case atom: CourseAtom       => courseAtomWrites.writes(atom)
  }

  implicit val courseDefaultWrites: Writes[CourseDefault] =
    Json.writes[CourseDefault]

  implicit val courseAtomWrites: Writes[CourseAtom] =
    Json.writes[CourseAtom]
}

object CourseFormat {
  trait All
      extends CourseFormat
      with CourseIntervalFormat
      with CourseTypeFormat
      with SubmoduleFormat.All
      with LanguageFormat
      with SeasonFormat
      with SemesterFormat.All
}
