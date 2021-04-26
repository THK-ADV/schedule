package database.repos

import database.tables.StudyProgramTable
import models.StudyProgram
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class StudyProgramRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[StudyProgram, StudyProgramTable] {

  import profile.api._

  protected val tableQuery = TableQuery[StudyProgramTable]

  override protected def makeFilter = {
    case ("label", vs)        => t => t.hasLabel(vs.head)
    case ("abbreviation", vs) => t => t.hasAbbreviation(vs.head)
    case ("graduation", vs)   => t => t.hasGraduation(vs.head)
  }
}
