package models

import java.util.UUID

case class CourseJson(
    lecturer: UUID,
    semester: UUID,
    subModule: UUID,
    interval: CourseInterval,
    courseType: CourseType
)
