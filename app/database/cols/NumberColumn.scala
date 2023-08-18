//package database.cols
//
//import slick.jdbc.PostgresProfile.api._
//
//trait NumberColumn {
//  self: Table[_] =>
//  def number = column[Int]("number")
//
//  def hasNumber(number: Int): Rep[Boolean] =
//    this.number === number
//}
