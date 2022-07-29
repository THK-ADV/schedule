package json

import models.SubModule.{SubModuleAtom, SubModuleDefault}
import models.{SubModule, SubModuleJson}
import play.api.libs.json.{Json, OFormat, Writes}

trait SubmoduleFormat
    extends ModuleFormat
    with LanguageFormat
    with SeasonFormat {
  implicit val submoduleJsonFmt: OFormat[SubModuleJson] =
    Json.format[SubModuleJson]

  implicit val submoduleWrites: Writes[SubModule] = Writes.apply {
    case default: SubModuleDefault => submoduleDefaultWrites.writes(default)
    case atom: SubModuleAtom       => submoduleAtomWrites.writes(atom)
  }

  implicit val submoduleDefaultWrites: Writes[SubModuleDefault] =
    Json.writes[SubModuleDefault]

  implicit val submoduleAtomWrites: Writes[SubModuleAtom] =
    Json.writes[SubModuleAtom]
}
