import play.api.mvc.{AnyContent, Request}

package object controllers {
  def preferredLanguage(implicit
      request: Request[AnyContent]
  ): PreferredLanguage =
    request.acceptLanguages
      .map(_.code)
      .headOption
      .map(a => PreferredLanguage(a.split('-').head))
      .getOrElse(PreferredLanguage.Default)
}
