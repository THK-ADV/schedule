package database.tables

import java.util.UUID

import slick.jdbc.PostgresProfile.api._

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
  ) <> (ModuleStudyProgramScheduleEntry.apply, ModuleStudyProgramScheduleEntry.unapply)
}
