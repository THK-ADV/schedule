package models

import localization.LocalizedLabel

case class Faculty(id: String, deLabel: String, enLabel: String)
    extends UniqueEntity[String]
    with LocalizedLabel
