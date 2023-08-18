//package database.cols
//
//import slick.jdbc.PostgresProfile.api._
//
//trait CreditsColumn {
//  self: Table[_] =>
//  def credits = column[Double]("ects")
//
//  def hasCredits(credits: Double): Rep[Boolean] =
//    this.credits === credits
//}
