package json

import models.{Graduation, GraduationJson}
import play.api.libs.json.{Json, OFormat}

trait GraduationFormat {
  implicit val graduationFmt: OFormat[Graduation] = Json.format[Graduation]

  implicit val graduationJsonFmt: OFormat[GraduationJson] =
    Json.format[GraduationJson]
}
