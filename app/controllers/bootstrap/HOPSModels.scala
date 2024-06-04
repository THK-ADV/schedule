package controllers.bootstrap

case class HOPSRoom(
    identifier: String,
    label: String,
    roomType: String,
    capacity: Int
)

case class HOPSScheduleEntry(
    roomIdentifier: String,
    weekIndex: Int,
    startStd: Int,
    course: String,
    part: String,
    moduleLabel: String,
    moduleId: Int,
    studyProgram: String,
    specialization: Option[String]
)
