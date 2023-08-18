//package database.cols
//
//import slick.jdbc.PostgresProfile.api._
//
//trait DescriptionUrlColumn {
//  self: Table[_] =>
//  def descriptionUrl = column[String]("description_file_url")
//
//  def hasDescriptionUrl(url: String): Rep[Boolean] =
//    this.descriptionUrl === url
//}
