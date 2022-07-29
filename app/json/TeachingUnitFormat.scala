package json

import models.{TeachingUnit, TeachingUnitJson}
import play.api.libs.json.{Json, OFormat}

trait TeachingUnitFormat {
  implicit val teachingUnitFmt: OFormat[TeachingUnit] =
    Json.format[TeachingUnit]
  implicit val teachingUnitJsonFmt: OFormat[TeachingUnitJson] =
    Json.format[TeachingUnitJson]
}
