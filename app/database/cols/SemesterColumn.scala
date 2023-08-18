//package database.cols
//
//import database.tables.SemesterTable
//import slick.jdbc.PostgresProfile.api._
//
//import java.util.UUID
//
//trait SemesterColumn {
//  self: Table[_] =>
//  def semester = column[UUID]("semester")
//
//  def semesterFk =
//    foreignKey("semester", semester, TableQuery[SemesterTable])(
//      _.id,
//      onUpdate = ForeignKeyAction.Restrict,
//      onDelete = ForeignKeyAction.Restrict
//    )
//
//  def semester(id: UUID): Rep[Boolean] = semester === id
//}
