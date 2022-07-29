package json

import models.CourseType
import play.api.libs.json.Format

trait CourseTypeFormat {
  implicit val courseTypeFmt: Format[CourseType] =
    Format.of[String].bimap(CourseType.apply, _.toString)
}
