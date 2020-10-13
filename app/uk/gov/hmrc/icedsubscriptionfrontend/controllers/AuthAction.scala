/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.icedsubscriptionfrontend.controllers

import javax.inject.Inject
import play.api.mvc.Results._
import play.api.mvc._
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.icedsubscriptionfrontend.config.AppConfig
import uk.gov.hmrc.icedsubscriptionfrontend.services.{AuthResult, AuthService}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendHeaderCarrierProvider

import scala.concurrent.{ExecutionContext, Future}

case class AuthenticatedRequest[A](request: Request[A], enrolled: Boolean)

class AuthAction @Inject()(defaultParser: PlayBodyParsers,
                           authService: AuthService,
                           appConfig: AppConfig)(
                            override implicit val executionContext: ExecutionContext)
  extends ActionBuilder[AuthenticatedRequest, AnyContent]
    with FrontendHeaderCarrierProvider {

  override def invokeBlock[A](request: Request[A], block: AuthenticatedRequest[A] => Future[Result]): Future[Result] = {
    implicit val headerCarrier: HeaderCarrier = hc(request)

    authService.authenticate().flatMap {
      case AuthResult.Enrolled => block(AuthenticatedRequest(request, enrolled = true))
      case AuthResult.NotEnrolled => block(AuthenticatedRequest(request, enrolled = false))
      case AuthResult.NotLoggedIn => Future.successful(Redirect(appConfig.loginUrl))
    }
  }

  override def parser: BodyParser[AnyContent] = defaultParser.defaultBodyParser
}
