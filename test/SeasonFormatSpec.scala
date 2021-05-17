import models.Season
import play.api.libs.json.{JsString, JsSuccess, Json}

class SeasonFormatSpec extends UnitSpec {

  "A Season Formatter" should {
    "convert from Season to JSON" in {
      val ss: Season = Season.SoSe
      val ws: Season = Season.WiSe
      val ss_ws: Season = Season.SoSe_WiSe

      Json.toJson(ss) shouldBe JsString("SoSe")
      Json.toJson(ws) shouldBe JsString("WiSe")
      Json.toJson(ss_ws) shouldBe JsString("SoSe_WiSe")
    }

    "convert from JSON to Season" in {
      val ss = "SoSe"
      val ws = "WiSe"
      val ss_ws = "SoSe_WiSe"
      val ws_ss = "WiSe_SoSe"

      Json.fromJson[Season](JsString(ss)) shouldBe JsSuccess(Season.SoSe)
      Json.fromJson[Season](JsString(ws)) shouldBe JsSuccess(Season.WiSe)
      Json.fromJson[Season](JsString(ss_ws)) shouldBe JsSuccess(
        Season.SoSe_WiSe
      )
      Json.fromJson[Season](JsString(ws_ss)) shouldBe JsSuccess(
        Season.SoSe_WiSe
      )
    }
  }
}
