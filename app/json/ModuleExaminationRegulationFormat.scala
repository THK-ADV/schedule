package json

import models.ModuleExaminationRegulation.{
  ModuleExaminationRegulationAtom,
  ModuleExaminationRegulationDefault
}
import models.{ModuleExaminationRegulation, ModuleExaminationRegulationJson}
import play.api.libs.json.{Json, OFormat, Writes}

trait ModuleExaminationRegulationFormat
    extends ExaminationRegulationFormat
    with ModuleFormat {

  implicit val moduleExamRegJsonFmt: OFormat[ModuleExaminationRegulationJson] =
    Json.format[ModuleExaminationRegulationJson]

  implicit val moduleExamRegWrites: Writes[ModuleExaminationRegulation] =
    Writes.apply {
      case default: ModuleExaminationRegulationDefault =>
        moduleExamRegDefaultWrites.writes(default)
      case atom: ModuleExaminationRegulationAtom =>
        moduleExamRegAtomWrites.writes(atom)
    }

  implicit val moduleExamRegDefaultWrites
      : Writes[ModuleExaminationRegulationDefault] =
    Json.writes[ModuleExaminationRegulationDefault]

  implicit val moduleExamRegAtomWrites
      : Writes[ModuleExaminationRegulationAtom] =
    Json.writes[ModuleExaminationRegulationAtom]
}
