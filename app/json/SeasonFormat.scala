package json

import models.Season
import play.api.libs.json.Format

trait SeasonFormat {
  implicit val seasonFmt: Format[Season] =
    Format.of[String].bimap(Season.apply, Season.unapply)
}
