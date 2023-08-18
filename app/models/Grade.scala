package models

import localization.{LocalizedDescription, LocalizedLabel}

case class Grade(
    id: String,
    deLabel: String,
    enLabel: String,
    deDesc: String,
    enDesc: String
) extends UniqueEntity[String]
    with LocalizedLabel
    with LocalizedDescription
