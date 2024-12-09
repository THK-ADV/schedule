package database.repos

import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext

import controllers.PreferredLanguage
import database.repos.abstracts.Create
import database.tables.SemesterPlanEntryTable
import database.view.JsonViewGetResult
import models.SemesterPlanEntry
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.jdbc.PositionedParameters
import slick.jdbc.SetParameter

@Singleton
final class SemesterPlanEntryRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Create[UUID, SemesterPlanEntry, SemesterPlanEntryTable]
    with JsonViewGetResult {

  import profile.api.*

  protected val tableQuery = TableQuery[SemesterPlanEntryTable]

  implicit val setLocalDateTime: SetParameter[LocalDateTime] =
    (v: LocalDateTime, pp: PositionedParameters) => pp.setTimestamp(Timestamp.valueOf(v))

  def allFromView(
      from: LocalDateTime,
      to: LocalDateTime,
      lang: PreferredLanguage
  ) = {
    val typeLabel = if (lang.isDe) "view.de_label" else "view.en_label"
    val semesterLabel =
      if (lang.isDe) "view.semester_de_label" else "view.semester_en_label"
    val sql =
      sql"""SELECT COALESCE(JSON_AGG(JSON_BUILD_OBJECT('id', view.id, 'start', view.start, 'end', view."end", 'type', JSON_BUILD_OBJECT('id',view.type,'label',#$typeLabel), 'semester', JSON_BUILD_OBJECT('id',view.semester,'index',view.semester_index,'label',#$semesterLabel), 'teachingUnit', view.teaching_unit)), '[]' :: json) AS semester_plan_entry FROM semester_plan_entry_view AS VIEW WHERE $to >= view.start and $from <= view.end""".as
    db.run(sql).map(_.head)
  }
}
