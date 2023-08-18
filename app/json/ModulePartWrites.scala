package json

import models.ModulePart
import play.api.libs.json.Writes

trait ModulePartWrites {
  implicit val modulePartsWrites: Writes[ModulePart] =
    Writes.of[String].contramap(_.value)
}
