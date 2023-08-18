package database

import pretty.PrettyPrinter

case class ModelAlreadyExistsException[A](entity: A, existing: A)
    extends Throwable {
  override def getMessage =
    s"""Model already exists
       |existing: ${PrettyPrinter.prettyPrint(existing)}
       |
       |to create: ${PrettyPrinter.prettyPrint(entity)}""".stripMargin
}
