package json

import models.{Faculty, FacultyJson}
import play.api.libs.json.{Json, OFormat}

trait FacultyFormat {
  implicit val facultyFmt: OFormat[Faculty] = Json.format[Faculty]

  implicit val facultyJsonFmt: OFormat[FacultyJson] = Json.format[FacultyJson]
}
