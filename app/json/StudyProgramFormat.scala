package json

import models.StudyProgram.{StudyProgramAtom, StudyProgramDefault}
import models.{StudyProgram, StudyProgramJson}
import play.api.libs.json.{Json, OFormat, Writes}

trait StudyProgramFormat
    extends JsonNullWritable
    with TeachingUnitFormat
    with GraduationFormat {
  implicit val studyProgramJsonFmt: OFormat[StudyProgramJson] =
    Json.format[StudyProgramJson]

  implicit val studyProgramWrites: Writes[StudyProgram] = Writes.apply {
    case default: StudyProgramDefault =>
      studyProgramDefaultWrites.writes(default)
    case atom: StudyProgramAtom => studyProgramAtomWrites.writes(atom)
  }

  implicit val studyProgramDefaultWrites: Writes[StudyProgramDefault] =
    Json.writes[StudyProgramDefault]

  implicit val studyProgramAtomWrites: Writes[StudyProgramAtom] =
    Json.writes[StudyProgramAtom]
}
