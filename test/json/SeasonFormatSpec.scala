package json

import models.Season
import play.api.libs.json.{JsString, JsSuccess, Json}
import suite.UnitSpec

class SeasonFormatSpec extends UnitSpec with SeasonWrites {

  "A Season Formatter" should {
    "convert from Season to JSON" in {
      val ss: Season = Season.SoSe
      val ws: Season = Season.WiSe
      val ss_ws: Season = Season.SoSe_WiSe
      val uk: Season = Season.Unknown

      Json.toJson(ss) shouldBe JsString("SoSe")
      Json.toJson(ws) shouldBe JsString("WiSe")
      Json.toJson(ss_ws) shouldBe JsString("SoSe_WiSe")
      Json.toJson(uk) shouldBe JsString("unknown")
    }

    "convert from JSON to Season" in {
      Json.fromJson[Season](JsString("SoSe")) shouldBe JsSuccess(Season.SoSe)
      Json.fromJson[Season](JsString("WiSe")) shouldBe JsSuccess(Season.WiSe)
      Json.fromJson[Season](JsString("SoSe_WiSe")) shouldBe JsSuccess(
        Season.SoSe_WiSe
      )
      Json.fromJson[Season](JsString("WiSe_SoSe")) shouldBe JsSuccess(
        Season.SoSe_WiSe
      )
      Json.fromJson[Season](JsString("other")) shouldBe JsSuccess(
        Season.Unknown
      )
    }
  }
}
