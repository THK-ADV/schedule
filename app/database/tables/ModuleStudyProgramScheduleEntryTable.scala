package database.tables

import slick.jdbc.PostgresProfile.api._

import java.util.UUID

case class ModuleStudyProgramScheduleEntry(
    scheduleEntry: UUID,
    moduleInStudProgram: UUID
)

final class ModuleStudyProgramScheduleEntryTable(tag: Tag)
    extends Table[ModuleStudyProgramScheduleEntry](
      tag,
      "module_in_study_program_placed_in_schedule_entry"
    ) {

  def scheduleEntry = column[UUID]("schedule_entry", O.PrimaryKey)

  def moduleInStudyProgram =
    column[UUID]("module_in_study_program", O.PrimaryKey)

  def * = (
    scheduleEntry,
    moduleInStudyProgram
  ) <> (ModuleStudyProgramScheduleEntry.tupled, ModuleStudyProgramScheduleEntry.unapply)
}
