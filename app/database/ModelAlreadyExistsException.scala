package database

case class ModelAlreadyExistsException[A](entity: A, existing: A)
    extends Throwable {
  override def getMessage = s"model $entity already exists: $existing"
}
