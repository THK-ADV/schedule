package models

import localization.LocalizedLabel

case class Language(id: String, deLabel: String, enLabel: String)
    extends UniqueEntity[String]
    with LocalizedLabel
