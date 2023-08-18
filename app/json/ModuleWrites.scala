package json

import models.{Module, ModuleType}
import play.api.libs.json.{Json, Writes}

trait ModuleWrites extends ModulePartWrites {
  implicit val moduleTypesWrites: Writes[ModuleType] =
    Writes.of[String].contramap(_.value)

  implicit val writes: Writes[Module] = Json.writes[Module]
}
