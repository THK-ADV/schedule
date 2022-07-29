package models

import java.util.UUID

case class SubModuleJson(
    module: UUID,
    label: String,
    abbreviation: String,
    recommendedSemester: Int,
    credits: Double,
    descriptionUrl: String,
    language: Language,
    season: Season
)
