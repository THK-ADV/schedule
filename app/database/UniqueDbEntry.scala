package database

import java.sql.Timestamp
import java.util.UUID

trait UniqueDbEntry {
  def id: UUID

  def lastModified: Timestamp
}
