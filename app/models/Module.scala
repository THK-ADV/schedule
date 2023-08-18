package models

import java.util.UUID

case class Module(
    id: UUID,
    label: String,
    abbrev: String,
    language: String,
    season: String,
    moduleType: ModuleType,
    active: Boolean,
    parts: List[ModulePart]
) extends UniqueEntity[UUID]
