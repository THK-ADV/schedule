package database.view

import slick.jdbc.GetResult

trait JsonViewGetResult {
  implicit def jsonResult: GetResult[String] =
    GetResult(_.nextString())
}
