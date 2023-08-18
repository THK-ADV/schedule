package database.repos

import database.tables.StudyProgramRelationTable
import models.StudyProgramRelation
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
final class StudyProgramRelationRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  val tableQuery = TableQuery[StudyProgramRelationTable]

  def createOrUpdateMany(
      elems: List[StudyProgramRelation]
  ): Future[Seq[StudyProgramRelation]] = {
    for {
      _ <- db.run(tableQuery.delete)
      res <- db.run((tableQuery returning tableQuery) ++= elems)
    } yield res
  }
}
