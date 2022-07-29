package json

import models.CourseInterval
import play.api.libs.json.{JsString, JsSuccess, Json}
import suite.UnitSpec

class CourseIntervalFormatSpec extends UnitSpec with CourseIntervalFormat {

  "A Course Interval Formatter" should {
    "convert from Course Interval to JSON" in {
      val reg: CourseInterval = CourseInterval.Regularly
      val irr: CourseInterval = CourseInterval.Irregularly
      val block: CourseInterval = CourseInterval.Block
      val uk: CourseInterval = CourseInterval.Unknown

      Json.toJson(reg) shouldBe JsString("regularly")
      Json.toJson(irr) shouldBe JsString("irregularly")
      Json.toJson(block) shouldBe JsString("block")
      Json.toJson(uk) shouldBe JsString("unknown")
    }

    "convert from JSON to Course Interval" in {
      Json.fromJson[CourseInterval](JsString("regularly")) shouldBe JsSuccess(
        CourseInterval.Regularly
      )
      Json.fromJson[CourseInterval](JsString("irregularly")) shouldBe JsSuccess(
        CourseInterval.Irregularly
      )
      Json.fromJson[CourseInterval](JsString("block")) shouldBe JsSuccess(
        CourseInterval.Block
      )
      Json.fromJson[CourseInterval](JsString("other")) shouldBe JsSuccess(
        CourseInterval.Unknown
      )
    }
  }
}
