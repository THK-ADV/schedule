package json

import models.Language
import play.api.libs.json.Format

trait LanguageFormat {
  implicit val languageFmt: Format[Language] =
    Format.of[String].bimap(Language.apply, Language.unapply)
}
