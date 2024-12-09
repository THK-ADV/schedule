package database.repos.abstracts

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import database.ModelAlreadyExistsException
import database.UniqueEntityColumn
import models.UniqueEntity
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

trait Create[
    ID,
    E <: UniqueEntity[ID],
    T <: slick.jdbc.PostgresProfile.api.Table[E] & UniqueEntityColumn[ID]
] {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  protected implicit def ctx: ExecutionContext

  protected def tableQuery: TableQuery[T]

  protected def uniqueCols(elem: E): List[T => Rep[Boolean]] = Nil

  final def create(elem: E): Future[E] = {
    def go() = tableQuery.returning(tableQuery) += elem

    val uniqueCols = this.uniqueCols(elem)
    val action =
      if (uniqueCols.isEmpty) go()
      else {
        val pred: T => Rep[Boolean] =
          uniqueCols.reduce((a, b) => c => a.apply(c) && b.apply(c))
        for {
          exists <- tableQuery
            .filter(pred)
            .result
          create <-
            if (exists.isEmpty) go()
            else DBIO.failed(ModelAlreadyExistsException(elem, exists.head))
        } yield create
      }
    db.run(action)
  }

  final def createMany(elems: List[E]): Future[Seq[E]] =
    db.run(tableQuery.returning(tableQuery) ++= elems)

  final def createOrUpdate(elem: E): Future[Option[E]] =
    db.run(createOrUpdateQuery(elem))

  final def createOrUpdateMany(elems: List[E]): Future[List[Option[E]]] =
    db.run(DBIO.sequence(elems.map(createOrUpdateQuery)))

  // None => Update
  // Some => Create
  private def createOrUpdateQuery(elem: E) =
    tableQuery.returning(tableQuery).insertOrUpdate(elem)
}
