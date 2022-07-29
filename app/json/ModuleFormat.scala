package json

import models.Module.{ModuleAtom, ModuleDefault}
import models.{Module, ModuleJson}
import play.api.libs.json.{Json, OFormat, Writes}

trait ModuleFormat extends UserFormat {
  implicit val moduleJsonFmt: OFormat[ModuleJson] = Json.format[ModuleJson]

  implicit val moduleWrites: Writes[Module] = Writes.apply {
    case default: ModuleDefault => moduleDefaultWrites.writes(default)
    case atom: ModuleAtom       => moduleAtomWrites.writes(atom)
  }

  implicit val moduleDefaultWrites: Writes[ModuleDefault] =
    Json.writes[ModuleDefault]

  implicit val moduleAtomWrites: Writes[ModuleAtom] =
    Json.writes[ModuleAtom]
}
