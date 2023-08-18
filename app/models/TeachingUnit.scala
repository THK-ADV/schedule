package models

import localization.LocalizedLabel

import java.util.UUID

case class TeachingUnit(
    id: UUID,
    faculty: String,
    deLabel: String,
    enLabel: String
) extends UniqueEntity[UUID]
    with LocalizedLabel
