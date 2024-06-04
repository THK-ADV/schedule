package database.repos.abstracts

import database.UniqueEntityColumn
import models.UniqueEntity
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api.Table

import scala.concurrent.{ExecutionContext, Future}

trait Get[
    ID,
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

  final def allWithFilter(filter: Filter): Future[Seq[E]] =
    parseFilter(filter)
      .map(xs => db.run(tableQuery.filter(combineFilter(xs)).result))
      .getOrElse(Future.successful(Nil))

  final def all(): Future[Seq[E]] =
    db.run(tableQuery.result)

  final def get(id: ID) =
    db.run(
      tableQuery.filter(_.hasID(id)).take(1).result.flatMap { xs =>
        xs.size match {
          case 1 =>
            DBIO.successful(xs.head)
          case 0 =>
            DBIO.failed(new Throwable(s"expected one element, but found none"))
          case _ =>
            DBIO.failed(new Throwable(s"expected one element, but found: $xs"))
        }
      }
    )

  final def getOpt(id: ID): Future[Option[E]] =
    db.run(tableQuery.filter(_.hasID(id)).take(1).result.map(_.headOption))

  private final def combineFilter(
      xs: List[T => Rep[Boolean]]
  ): T => Rep[Boolean] =
    xs.reduceLeftOption[T => Rep[Boolean]]((lhs, rhs) =>
      t => lhs.apply(t) && rhs.apply(t)
    ).getOrElse(_ => true)

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
}
