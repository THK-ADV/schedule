package database.repos

import controllers.PreferredLanguage
import database.repos.abstracts.Create
import database.tables.SemesterPlanEntryTable
import database.view.JsonViewGetResult
import models.SemesterPlanEntry
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.{JdbcProfile, PositionedParameters, SetParameter}

import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class SemesterPlanEntryRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Create[UUID, SemesterPlanEntry, SemesterPlanEntryTable]
    with JsonViewGetResult {

  import profile.api._

  protected val tableQuery = TableQuery[SemesterPlanEntryTable]

  implicit val setLocalDateTime: SetParameter[LocalDateTime] =
    (v: LocalDateTime, pp: PositionedParameters) =>
      pp.setTimestamp(Timestamp.valueOf(v))

  def allFromView(
      referenceDate: LocalDateTime,
      lang: PreferredLanguage
  ) = {
    val typeLabel = if (lang.isDe) "view.de_label" else "view.en_label"
    val semesterLabel =
      if (lang.isDe) "view.semester_de_label" else "view.semester_en_label"
    val sql =
      sql"""SELECT COALESCE(JSON_AGG(JSON_BUILD_OBJECT('id', view.id, 'start', view.start, 'end', view."end", 'type', JSON_BUILD_OBJECT('id',view.type,'label',#$typeLabel), 'semester', JSON_BUILD_OBJECT('id',view.semester,'index',view.semester_index,'label',#$semesterLabel), 'teachingUnit', view.teaching_unit)), '[]' :: json) AS semester_plan_entry FROM semester_plan_entry_view AS VIEW WHERE $referenceDate >= view.semester_start and $referenceDate <= view.semester_end""".as
    db.run(sql).map(_.head)
  }
}
