package json

import models.CourseType
import play.api.libs.json.{JsString, Reads, Writes}

trait CourseTypeFormat { // TODO format
  implicit val courseTypeWrites: Writes[CourseType] =
    Writes[CourseType](CourseType.unapply _ andThen JsString)

  implicit val courseTypeReads: Reads[CourseType] =
    Reads(_.validate[String].map(CourseType.apply))
}
