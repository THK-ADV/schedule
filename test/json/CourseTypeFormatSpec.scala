package json

import models.CourseType
import play.api.libs.json.{JsString, JsSuccess, Json}
import suite.UnitSpec

class CourseTypeFormatSpec extends UnitSpec with CourseTypeFormat {

  "A Course Type Formatter" should {
    "convert from Course Type to JSON" in {
      val lec: CourseType = CourseType.Lecture
      val prac: CourseType = CourseType.Practical
      val exer: CourseType = CourseType.Exercise
      val tut: CourseType = CourseType.Tutorial
      val sem: CourseType = CourseType.Seminar
      val uk: CourseType = CourseType.Unknown

      Json.toJson(lec) shouldBe JsString("lecture")
      Json.toJson(prac) shouldBe JsString("practical")
      Json.toJson(exer) shouldBe JsString("exercise")
      Json.toJson(tut) shouldBe JsString("tutorial")
      Json.toJson(sem) shouldBe JsString("seminar")
      Json.toJson(uk) shouldBe JsString("unknown")
    }

    "convert from JSON to Course Type" in {
      Json.fromJson[CourseType](JsString("lecture")) shouldBe JsSuccess(
        CourseType.Lecture
      )
      Json.fromJson[CourseType](JsString("practical")) shouldBe JsSuccess(
        CourseType.Practical
      )
      Json.fromJson[CourseType](JsString("exercise")) shouldBe JsSuccess(
        CourseType.Exercise
      )
      Json.fromJson[CourseType](JsString("tutorial")) shouldBe JsSuccess(
        CourseType.Tutorial
      )
      Json.fromJson[CourseType](JsString("seminar")) shouldBe JsSuccess(
        CourseType.Seminar
      )
      Json.fromJson[CourseType](JsString("other")) shouldBe JsSuccess(
        CourseType.Unknown
      )
    }
  }
}
