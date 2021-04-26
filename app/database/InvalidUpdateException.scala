package database

case class InvalidUpdateException[A](entity: A) extends Throwable {
  override def getMessage =
    s"invalid update of $entity"
}
