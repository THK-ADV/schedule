package json

import models.ExaminationRegulation.{
  ExaminationRegulationAtom,
  ExaminationRegulationDefault
}
import models.{ExaminationRegulation, ExaminationRegulationJson}
import play.api.libs.json.{Json, OFormat, Writes}

trait ExaminationRegulationFormat
    extends JsonNullWritable
    with LocalDateFormat
    with StudyProgramFormat {
  implicit val examRegJsonFmt: OFormat[ExaminationRegulationJson] =
    Json.format[ExaminationRegulationJson]

  implicit val examRegWrites: Writes[ExaminationRegulation] = Writes.apply {
    case default: ExaminationRegulationDefault =>
      examRegDefaultWrites.writes(default)
    case atom: ExaminationRegulationAtom => examRegAtomWrites.writes(atom)
  }

  implicit val examRegDefaultWrites: Writes[ExaminationRegulationDefault] =
    Json.writes[ExaminationRegulationDefault]

  implicit val examRegAtomWrites: Writes[ExaminationRegulationAtom] =
    Json.writes[ExaminationRegulationAtom]
}
