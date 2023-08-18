//package database.repos.filter
//
//import database.cols.CourseColumn
//import models.{CourseInterval, CourseType, Language, Season}
//import play.api.db.slick.HasDatabaseConfigProvider
//import slick.jdbc.JdbcProfile
//
//trait CourseFilter[T <: CourseColumn] extends UUIDParser with DoubleParser {
//  self: HasDatabaseConfigProvider[JdbcProfile] =>
//
//  import profile.api._
//
//  def allCourse: PartialFunction[(String, Seq[String]), T => Rep[Boolean]] =
//    course orElse subModule orElse lecturer orElse semester
//
//  def course: PartialFunction[(String, Seq[String]), T => Rep[Boolean]] = {
//    case ("course", vs)          => t => parseUUID(vs, t.course)
//    case ("course_interval", vs) => _.courseInterval(CourseInterval(vs.head))
//    case ("course_type", vs)     => _.courseType(CourseType(vs.head))
//  }
//
//  def subModule: PartialFunction[(String, Seq[String]), T => Rep[Boolean]] = {
//    case ("subModule", vs)              => t => parseUUID(vs, t.subModule)
//    case ("subModule_label", vs)        => _.subModuleLabel(vs.head)
//    case ("subModule_abbreviation", vs) => _.subModuleAbbreviation(vs.head)
//    case ("subModule_season", vs)       => _.subModuleSeason(Season(vs.head))
//    case ("subModule_language", vs)     => _.subModuleLang(Language(vs.head))
//    case ("subModule_credits", vs)      => t => parseDouble(vs, t.subModuleCredits)
//    case ("subModule_module", vs)       => t => parseUUID(vs, t.subModuleModule)
//  }
//
//  def lecturer: PartialFunction[(String, Seq[String]), T => Rep[Boolean]] = {
//    case ("lecturer", vs)           => t => parseUUID(vs, t.lecturer)
//    case ("lecturer_username", vs)  => _.lecturerUsername(vs.head)
//    case ("lecturer_firstname", vs) => _.lecturerFirstname(vs.head)
//    case ("lecturer_lastname", vs)  => _.lecturerLastname(vs.head)
//    case ("lecturer_status", vs)    => _.lecturerStatus(vs.head)
//  }
//
//  def semester: PartialFunction[(String, Seq[String]), T => Rep[Boolean]] = {
//    case ("semester", vs) => t => parseUUID(vs, t.semester)
//  }
//}
