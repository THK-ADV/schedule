package database.repos

import database.cols.IDColumn
import database.{InvalidUpdateException, ModelAlreadyExistsException}
import models.UniqueEntity
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api.Table

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

trait Repository[E <: UniqueEntity, T <: Table[E] with IDColumn] {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  protected def tableQuery: TableQuery[T]

  protected implicit def ctx: ExecutionContext

  protected def makeFilter
      : PartialFunction[(String, Seq[String]), T => Rep[Boolean]]

  final def list(filter: Map[String, Seq[String]]): Future[Seq[E]] = {
    def performFilter(list: List[T => Rep[Boolean]]) = list match {
      case x :: xs =>
        xs.foldLeft(tableQuery.filter(x)) { (acc, next) =>
          acc.filter(next)
        }
      case Nil =>
        tableQuery
    }

    def run(q: Query[T, E, Seq]) =
      db.run(q.result)

    parseFilter(filter)
      .map(performFilter _ andThen run)
      .getOrElse(Future.successful(Nil))
  }

  final def get(id: UUID): Future[Option[E]] =
    db.run(tableQuery.filter(_.hasID(id)).take(1).result.headOption)

  final def delete(id: UUID): Future[E] =
    single(id) { (existing, q) =>
      for (del <- q.delete if del > 0) yield existing
    }

  final def update(elem: E, canUpdate: E => Boolean): Future[E] =
    single(elem.id) { (existing, q) =>
      for {
        _ <-
          if (canUpdate(existing)) q.update(elem)
          else DBIO.failed(InvalidUpdateException(elem, existing))
      } yield elem
    }

  final def single[A](existing: UUID)(
      f: (E, Query[T, E, Seq]) => DBIOAction[A, NoStream, Effect.Write]
  ): Future[A] = {
    val q = tableQuery.filter(_.hasID(existing))

    db.run {
      for {
        xs <- q.result
        existing <-
          if (xs.size == 1) DBIO.successful(xs.head)
          else
            DBIO.failed(
              new Throwable(s"None or more than one element found: $xs")
            )
        r <- f(existing, q)
      } yield r
    }
  }

  final def create(elem: E, uniqueCols: T => List[Rep[Boolean]]): Future[E] =
    db.run {
      for {
        exists <- tableQuery
          .filter(uniqueCols(_).reduce(_ && _))
          .result
        create <-
          if (exists.isEmpty) tableQuery returning tableQuery += elem
          else DBIO.failed(ModelAlreadyExistsException(elem, exists.head))
      } yield create
    }

  private def parseFilter(
      filter: Map[String, Seq[String]]
  ): Option[List[T => Rep[Boolean]]] = {
    filter.foldLeft(Option.apply(List.empty[T => Rep[Boolean]])) {
      case (xs, x) =>
        if (makeFilter.isDefinedAt(x))
          xs.map(makeFilter.apply(x) :: _)
        else
          xs
      case _ =>
        None
    }
  }
}
