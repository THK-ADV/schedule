//package database.cols
//
//import database.tables.SubModuleTable
//import models.{Language, Season}
//import slick.jdbc.PostgresProfile.api._
//
//import java.util.UUID
//
//trait SubModuleColumn {
//  self: Table[_] =>
//  def subModule = column[UUID]("submodule")
//
//  def subModuleFk =
//    foreignKey("submodule", subModule, TableQuery[SubModuleTable])(
//      _.id,
//      onUpdate = ForeignKeyAction.Restrict,
//      onDelete = ForeignKeyAction.Restrict
//    )
//
//  def subModule(id: UUID): Rep[Boolean] = subModule === id
//
//  def subModuleLabel(label: String) =
//    subModuleFk.filter(_.hasLabel(label)).exists
//
//  def subModuleAbbreviation(abbrev: String) =
//    subModuleFk.filter(_.hasAbbreviation(abbrev)).exists
//
//  def subModuleSeason(season: Season) =
//    subModuleFk.filter(_.hasSeason(season)).exists
//
//  def subModuleLang(language: Language) =
//    subModuleFk.filter(_.hasLanguage(language)).exists
//
//  def subModuleCredits(credits: Double) =
//    subModuleFk.filter(_.hasCredits(credits)).exists
//
//  def subModuleModule(id: UUID) =
//    subModuleFk.filter(_.isModule(id)).exists
//}
