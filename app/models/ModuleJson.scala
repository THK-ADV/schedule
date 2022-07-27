package models

import java.util.UUID

case class ModuleJson(
    courseManager: UUID,
    label: String,
    abbreviation: String,
    credits: Double,
    descriptionUrl: String
)
