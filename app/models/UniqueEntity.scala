package models

trait UniqueEntity[A] {
  def id: A
}
