package database

import java.util.UUID

import slick.ast.BaseTypedType
import slick.jdbc.PostgresProfile.api._

trait UniqueEntityColumn[A] {
  self: Table[?] =>
  implicit def tt: BaseTypedType[A]
  def id = column[A]("id", O.PrimaryKey)

  def hasID(id: A) = this.id === id
}

trait StringUniqueColumn extends UniqueEntityColumn[String] { self: Table[?] =>
  implicit override def tt: BaseTypedType[String] = stringColumnType
}

trait UUIDUniqueColumn extends UniqueEntityColumn[UUID] { self: Table[?] =>
  implicit override def tt: BaseTypedType[UUID] = uuidColumnType
}
