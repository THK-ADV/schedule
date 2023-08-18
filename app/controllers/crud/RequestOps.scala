package controllers.crud

import play.api.mvc.{AnyContent, BaseController, Request}

trait RequestOps { self: BaseController =>
  implicit class RebaseRequest[A](val request: Request[A]) {
    def appending(query: (String, Seq[String])*): Request[A] = {
      val newQs = query.foldLeft(request.queryString)(_ + _)
      request.withTarget(request.target.withQueryString(newQs))
    }

    def eraseToAnyContent() = request.withBody(AnyContent())
  }
}
