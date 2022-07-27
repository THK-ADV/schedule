package json

import models.Language
import play.api.libs.json.{JsString, JsSuccess, Json}
import suite.UnitSpec

class LanguageFormatSpec extends UnitSpec with LanguageFormat {

  "A Language Formatter" should {
    "convert from Language to JSON" in {
      val de: Language = Language.DE
      val en: Language = Language.EN
      val de_en: Language = Language.DE_EN
      val uk: Language = Language.Unknown

      Json.toJson(de) shouldBe JsString("de")
      Json.toJson(en) shouldBe JsString("en")
      Json.toJson(de_en) shouldBe JsString("de_en")
      Json.toJson(uk) shouldBe JsString("unknown")
    }

    "convert from JSON to Language" in {
      Json.fromJson[Language](JsString("de")) shouldBe JsSuccess(Language.DE)
      Json.fromJson[Language](JsString("en")) shouldBe JsSuccess(Language.EN)
      Json.fromJson[Language](JsString("de_en")) shouldBe JsSuccess(
        Language.DE_EN
      )
      Json.fromJson[Language](JsString("en_de")) shouldBe JsSuccess(
        Language.DE_EN
      )
      Json.fromJson[Language](JsString("other")) shouldBe JsSuccess(
        Language.Unknown
      )
    }
  }
}
