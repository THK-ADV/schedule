package json

import models.{Campus, CampusJson}
import play.api.libs.json.{Json, OFormat}

trait CampusFormat {
  implicit val campusFmt: OFormat[Campus] = Json.format[Campus]

  implicit val campusJsonFmt: OFormat[CampusJson] = Json.format[CampusJson]
}
