package database.repos.abstracts

import database.cols.UniqueEntityColumn
import models.UniqueEntity
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api.Table

import scala.concurrent.{ExecutionContext, Future}

trait Get[
    ID,
    M <: UniqueEntity[ID],
    E <: UniqueEntity[ID],
    T <: Table[E] with UniqueEntityColumn[ID]
] { self: HasDatabaseConfigProvider[JdbcProfile] =>
  import profile.api._

  type Filter = Map[String, Seq[String]]

  protected implicit def ctx: ExecutionContext

  protected def tableQuery: TableQuery[T]

  protected def makeFilter
      : PartialFunction[(String, Seq[String]), T => Rep[Boolean]] =
    PartialFunction.empty

  protected def retrieveDefault(query: Query[T, E, Seq]): Future[Seq[M]] =
    db.run(query.result.map(_.map(toUniqueEntity)))

  protected def retrieveAtom(query: Query[T, E, Seq]): Future[Seq[M]]

  protected def toUniqueEntity(e: E): M

  final def combineFilter(xs: List[T => Rep[Boolean]]): T => Rep[Boolean] =
    xs.reduceLeftOption[T => Rep[Boolean]]((lhs, rhs) =>
      t => lhs.apply(t) && rhs.apply(t)
    ).getOrElse(_ => true)

  final def list(filter: Filter, atomic: Boolean): Future[Seq[M]] =
    parseFilter(filter)
      .map(xs => retrieve(atomic)(tableQuery.filter(combineFilter(xs))))
      .getOrElse(Future.successful(Nil))

  final def get(id: ID, atomic: Boolean): Future[Option[M]] =
    retrieve(atomic)(tableQuery.filter(_.hasID(id)).take(1))
      .map(_.headOption)

  final def single[A](existing: ID)(
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
