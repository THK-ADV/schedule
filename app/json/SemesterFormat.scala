package json

import models.{Semester, SemesterJson}
import play.api.libs.json.{Json, OFormat}

trait SemesterFormat extends LocalDateFormat {
  implicit val semesterFmt: OFormat[Semester] = Json.format[Semester]
  implicit val semesterJsonFmt: OFormat[SemesterJson] =
    Json.format[SemesterJson]
}
