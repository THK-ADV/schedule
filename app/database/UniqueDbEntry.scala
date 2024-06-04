package database

trait UniqueDbEntry[ID] {
  def id: ID
}
