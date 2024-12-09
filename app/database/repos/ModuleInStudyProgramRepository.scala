package database.repos

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import database.repos.abstracts.Create
import database.repos.abstracts.Get
import database.tables.ModuleInStudyProgramTable
import database.tables.ModuleRelationTable
import database.tables.ModuleTable
import models.CourseId
import models.Module
import models.ModuleInStudyProgram
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

@Singleton
final class ModuleInStudyProgramRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Get[UUID, ModuleInStudyProgram, ModuleInStudyProgramTable]
    with Create[UUID, ModuleInStudyProgram, ModuleInStudyProgramTable] {

  import database.tables.modulePartsColumnType
  import profile.api._

  protected val tableQuery = TableQuery[ModuleInStudyProgramTable]

  def deleteAll() =
    db.run(tableQuery.delete)

  def allModulesInSeason(seasons: Seq[String]): Future[Seq[Module]] =
    db.run(
      TableQuery[ModuleTable]
        .filter { module =>
          val inSeason = module.season.inSet(seasons)
          val hasParts = !(module.parts === List.empty[CourseId])
          val noParent = !TableQuery[ModuleRelationTable]
            .filter(_.parent === module.id)
            .exists
          inSeason && hasParts && noParent
        }
        .join(tableQuery.map(_.module))
        .on(_.id === _)
        .map(_._1)
        .distinctOn(_.id)
        .result
    )
}
