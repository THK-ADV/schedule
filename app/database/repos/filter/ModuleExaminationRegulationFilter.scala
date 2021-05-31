package database.repos.filter

import database.cols.ModuleExaminationRegulationColumn
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

trait ModuleExaminationRegulationFilter[T <: ModuleExaminationRegulationColumn]
    extends UUIDParser
    with BooleanParser
    with IntParser
    with DateTimeParser {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  def allModuleExaminationRegulations
      : PartialFunction[(String, Seq[String]), T => Rep[Boolean]] =
    moduleExaminationRegulation orElse examinationRegulation orElse studyProgram orElse module

  def moduleExaminationRegulation
      : PartialFunction[(String, Seq[String]), T => Rep[Boolean]] = {
    case ("moduleExaminationRegulation", vs) =>
      t => parseUUID(vs, t.moduleExaminationRegulation)
    case ("moduleExaminationRegulation_mandatory", vs) =>
      t => parseBoolean(vs, t.isMandatory)
    case ("module", vs) => t => parseUUID(vs, t.module)
  }

  def examinationRegulation
      : PartialFunction[(String, Seq[String]), T => Rep[Boolean]] = {
    case ("exams", vs) =>
      t => parseUUID(vs, t.examinationRegulation)
    case ("exams_number", vs) =>
      t => parseInt(vs, t.examinationRegulationNumber)
    case ("exams_onStart", vs) =>
      t => parseDate(vs, t.examinationRegulationOnStart)
    case ("exams_sinceStart", vs) =>
      t => parseDate(vs, t.examinationRegulationSinceStart)
    case ("exams_onEnd", vs) => t => parseDate(vs, t.examinationRegulationOnEnd)
    case ("exams_untilEnd", vs) =>
      t => parseDate(vs, t.examinationRegulationUntilEnd)
  }

  def studyProgram
      : PartialFunction[(String, Seq[String]), T => Rep[Boolean]] = {
    case ("studyProgram", vs)       => t => parseUUID(vs, t.studyProgram)
    case ("studyProgram_label", vs) => _.studyProgramLabel(vs.head)
    case ("studyProgram_abbreviation", vs) =>
      _.studyProgramAbbreviation(vs.head)
    case ("studyProgram_graduation", vs) =>
      t => parseUUID(vs, t.studyProgramGraduation)
    case ("studyProgram_teachingUnit", vs) =>
      t => parseUUID(vs, t.studyProgramTeachingUnit)
  }

  def module: PartialFunction[(String, Seq[String]), T => Rep[Boolean]] = {
    case ("module", vs) => t => parseUUID(vs, t.module)
  }
}
