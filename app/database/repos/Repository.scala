package database.repos

import database.cols.UniqueEntityColumn
import database.{InvalidUpdateException, ModelAlreadyExistsException, UniqueDbEntry}
import models.UniqueEntity
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api.Table

import java.sql.Timestamp
import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

trait Repository[M <: UniqueEntity, E <: UniqueDbEntry, T <: Table[
  E
] with UniqueEntityColumn] {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  type Filter = Map[String, Seq[String]]

  protected def tableQuery: TableQuery[T]

  protected implicit def ctx: ExecutionContext

  protected def makeFilter
      : PartialFunction[(String, Seq[String]), T => Rep[Boolean]]

  protected def retrieveDefault(query: Query[T, E, Seq]): Future[Seq[M]] =
    db.run(query.result.map(_.map(toUniqueEntity)))

  protected def retrieveAtom(query: Query[T, E, Seq]): Future[Seq[M]]

  protected def toUniqueEntity(e: E): M

  private def performFilter(list: List[T => Rep[Boolean]]) = list match {
    case x :: xs =>
      xs.foldLeft(tableQuery.filter(x)) { (acc, next) =>
        acc.filter(next)
      }
    case Nil =>
      tableQuery
  }

  final def count(filter: Filter) =
    parseFilter(filter) match {
      case Some(f) =>
        db.run(performFilter(f).size.result)
      case None =>
        db.run(tableQuery.size.result)
    }

  final def list(filter: Filter, atomic: Boolean): Future[Seq[M]] =
    parseFilter(filter)
      .map(performFilter _ andThen retrieve(atomic))
      .getOrElse(Future.successful(Nil))

  final def get(id: UUID, atomic: Boolean): Future[Option[M]] =
    retrieve(atomic)(tableQuery.filter(_.hasID(id)).take(1))
      .map(_.headOption)

  final def delete(id: UUID): Future[M] =
    single(id) { (existing, q) =>
      for {
        del <- q.delete if del > 0
      } yield toUniqueEntity(existing)
    }

  final def update(elem: E, canUpdate: E => Boolean): Future[M] = {
    def go(q: Query[T, E, Seq]) = for {
      u1 <- q.update(elem)
      u2 <- q
        .map(_.lastModified)
        .update(new Timestamp(System.currentTimeMillis()))
    } yield u1 + u2

    single(elem.id) { (existing, q) =>
      for {
        _ <-
          if (canUpdate(existing)) go(q)
          else DBIO.failed(InvalidUpdateException(elem, existing))
      } yield toUniqueEntity(elem)
    }
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

  final def create(
      elem: E,
      uniqueCols: List[T => Rep[Boolean]]
  ): Future[M] = {
    def create0 =
      tableQuery returning tableQuery += elem

    def createIfUnique =
      for {
        exists <- tableQuery
          .filter(t =>
            uniqueCols.foldLeft(uniqueCols.head(t))((acc, f) => acc && f(t))
          )
          .result
        create <-
          if (exists.isEmpty) create0
          else DBIO.failed(ModelAlreadyExistsException(elem, exists.head))
      } yield create

    val action = if (uniqueCols.isEmpty) create0 else createIfUnique
    db.run(action.map(toUniqueEntity))
  }

  final def forceInsert(es: Seq[E]) =
    db.run(tableQuery ++= es).map(_.getOrElse(0))

  private def parseFilter(
      filter: Filter
  ): Option[List[T => Rep[Boolean]]] = {
    filter.foldLeft(Option.apply(List.empty[T => Rep[Boolean]])) {
      case (xs, x) =>
        if (x._2.nonEmpty && makeFilter.isDefinedAt(x))
          xs.map(makeFilter.apply(x) :: _)
        else
          xs
      case _ =>
        None
    }
  }

  private def retrieve(atomic: Boolean)(q: Query[T, E, Seq]) =
    if (atomic) retrieveAtom(q)
    else retrieveDefault(q)
}
