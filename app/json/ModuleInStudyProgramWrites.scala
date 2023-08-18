package json

import models.ModuleInStudyProgram
import play.api.libs.json.{Json, Writes}

trait ModuleInStudyProgramWrites {
  implicit val writes: Writes[ModuleInStudyProgram] = Json.writes
}
