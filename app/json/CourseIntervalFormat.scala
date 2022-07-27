package json

import models.CourseInterval
import play.api.libs.json.Format

trait CourseIntervalFormat {
  implicit val courseIntervalFmt: Format[CourseInterval] =
    Format.of[String].bimap(CourseInterval.apply, CourseInterval.unapply)
}
