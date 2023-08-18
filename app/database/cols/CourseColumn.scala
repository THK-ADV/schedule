//package database.cols
//
//import database.tables.CourseTable
//import models.{CourseInterval, CourseType, Language, Season}
//import slick.jdbc.PostgresProfile.api._
//
//import java.util.UUID
//
//trait CourseColumn {
//  self: Table[_] =>
//  def course = column[UUID]("course")
//
//  def course(id: UUID): Rep[Boolean] = course === id
//
//  def courseType(ct: CourseType) =
//    courseFk.filter(_.hasCourseType(ct)).exists
//
//  def courseInterval(i: CourseInterval) =
//    courseFk.filter(_.hasInterval(i)).exists
//
//  def subModule(id: UUID) =
//    courseFk.filter(_.subModule(id)).exists
//
//  def subModuleLabel(label: String) =
//    courseFk.filter(_.subModuleLabel(label)).exists
//
//  def subModuleAbbreviation(abbrev: String) =
//    courseFk.filter(_.subModuleAbbreviation(abbrev)).exists
//
//  def subModuleSeason(season: Season) =
//    courseFk.filter(_.subModuleSeason(season)).exists
//
//  def subModuleLang(language: Language) =
//    courseFk.filter(_.subModuleLang(language)).exists
//
//  def subModuleCredits(credits: Double) =
//    courseFk.filter(_.subModuleCredits(credits)).exists
//
//  def subModuleModule(id: UUID) =
//    courseFk.filter(_.subModuleModule(id)).exists
//
//  def lecturer(id: UUID) =
//    courseFk.filter(_.user(id)).exists
//
//  def lecturerUsername(name: String) =
//    courseFk.filter(_.hasUsername(name)).exists
//
//  def lecturerStatus(status: String) =
//    courseFk.filter(_.hasStatus(status)).exists
//
//  def lecturerFirstname(name: String) =
//    courseFk.filter(_.hasFirstname(name)).exists
//
//  def lecturerLastname(name: String) =
//    courseFk.filter(_.hasLastname(name)).exists
//
//  def semester(id: UUID) =
//    courseFk.filter(_.semester(id)).exists
//
//  def courseFk =
//    foreignKey("course", course, TableQuery[CourseTable])(
//      _.id,
//      onUpdate = ForeignKeyAction.Restrict,
//      onDelete = ForeignKeyAction.Restrict
//    )
//}
