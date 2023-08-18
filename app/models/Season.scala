package models

import localization.LocalizedLabel

case class Season(id: String, deLabel: String, enLabel: String)
    extends UniqueEntity[String]
    with LocalizedLabel
