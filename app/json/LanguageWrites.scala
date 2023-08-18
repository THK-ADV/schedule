package json

import models.Language
import play.api.libs.json.{Json, Writes}

trait LanguageWrites {
  implicit val writes: Writes[Language] = Json.writes[Language]
}
