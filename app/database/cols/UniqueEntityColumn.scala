package database.cols

import slick.ast.BaseTypedType
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

trait UniqueEntityColumn[A] {
  self: Table[_] =>
  implicit def tt: BaseTypedType[A]
  def id = column[A]("id", O.PrimaryKey)

  def hasID(id: A) = this.id === id
}

trait StringUniqueColumn extends UniqueEntityColumn[String] { self: Table[_] =>
  override implicit def tt: BaseTypedType[String] = stringColumnType
}

trait UUIDUniqueColumn extends UniqueEntityColumn[UUID] { self: Table[_] =>
  override implicit def tt: BaseTypedType[UUID] = uuidColumnType
}
