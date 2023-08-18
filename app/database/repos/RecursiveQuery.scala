//package database.repos
//import database.UniqueDbEntry
//import database.cols.UniqueEntityColumn
//import play.api.db.slick.HasDatabaseConfigProvider
//import slick.jdbc.JdbcProfile
//import slick.jdbc.PostgresProfile.api.Table
//import slick.sql.FixedSqlStreamingAction
//
//import java.util.UUID
//import scala.concurrent.ExecutionContext
//
//trait RecursiveQuery[
//    TableElem <: UniqueDbEntry,
//    TableDep,
//    Atom,
//    T <: Table[TableElem] with UniqueEntityColumn
//] { self: HasDatabaseConfigProvider[JdbcProfile] =>
//  import profile.api._
//
//  protected implicit def ctx: ExecutionContext
//
//  protected def tableQuery: TableQuery[T]
//
//  protected def parentId(e: TableElem): Option[UUID]
//
//  protected def query(
//      ids: Seq[UUID]
//  ): FixedSqlStreamingAction[Seq[TableDep], TableDep, Effect.Read]
//
//  protected def makeAtom(lookup: Map[UUID, TableDep], e: TableDep): Atom
//
//  protected def elem(es: TableDep): TableElem
//
//  private def collectParents(
//      xs: Seq[TableDep]
//  ): DBIOAction[Map[UUID, TableDep], NoStream, Effect.Read] = {
//    // O(3n)
//    def go(
//        elements: Seq[TableDep],
//        map: Map[UUID, TableDep]
//    ): DBIOAction[Map[UUID, TableDep], NoStream, Effect.Read] = {
//      if (elements.isEmpty) return DBIO.successful(map)
//
//      val newMap = elements.foldLeft(map) { case (acc, p) =>
//        acc + ((elem(p).id, p))
//      }
//
//      val parentIds = elements.foldLeft(List.empty[UUID]) { case (acc, e) =>
//        parentId(elem(e)).fold(acc)(_ :: acc)
//      }
//
//      query(parentIds).flatMap(go(_, newMap))
//    }
//
//    go(xs, Map.empty)
//  }
//
//  def getRecursive(
//      xs: Seq[TableDep]
//  ): DBIOAction[Seq[Atom], NoStream, Effect.Read] =
//    collectParents(xs).map(m => xs.map(e => makeAtom(m, e)))
//}
