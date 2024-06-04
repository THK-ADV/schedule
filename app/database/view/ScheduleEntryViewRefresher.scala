package database.view

import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

trait ScheduleEntryViewRefresher extends MaterializedView {
  self: HasDatabaseConfigProvider[JdbcProfile] =>
  override def name: String = "schedule_entry_view"
}
