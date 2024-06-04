package database.repos

import database.repos.abstracts.{Create, Get}
import database.tables.{
  ModuleInStudyProgramTable,
  ModuleRelationTable,
  ModuleTable
}
import models.{Module, ModuleInStudyProgram, CourseId}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

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
