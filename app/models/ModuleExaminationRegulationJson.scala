package models

import java.util.UUID

case class ModuleExaminationRegulationJson(
    module: UUID,
    examinationRegulation: UUID,
    mandatory: Boolean
)
