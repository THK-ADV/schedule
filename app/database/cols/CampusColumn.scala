//package database.cols
//
//import database.tables.CampusTable
//import slick.jdbc.PostgresProfile.api._
//
//import java.util.UUID
//
//trait CampusColumn {
//  self: Table[_] =>
//  def campus = column[UUID]("campus")
//
//  def campus(id: UUID): Rep[Boolean] = campus === id
//
//  def label(label: String): Rep[Boolean] =
//    campusFk.filter(_.hasLabel(label)).exists
//
//  def abbrev(abbrev: String): Rep[Boolean] =
//    campusFk.filter(_.hasAbbreviation(abbrev)).exists
//
//  def campusFk =
//    foreignKey("campus", campus, TableQuery[CampusTable])(
//      _.id,
//      onUpdate = ForeignKeyAction.Restrict,
//      onDelete = ForeignKeyAction.Restrict
//    )
//}
