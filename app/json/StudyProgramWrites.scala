package json

import models.StudyProgram
import play.api.libs.json.{Json, Writes}

trait StudyProgramWrites extends JsonNullWritable with LocalDateFormat {
  implicit val writes: Writes[StudyProgram] =
    Json.writes[StudyProgram]
}
