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

import controllers.Assets.Redirect
import javax.inject.Inject
import play.api.mvc._
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.icedsubscriptionfrontend.config.AppConfig
import uk.gov.hmrc.icedsubscriptionfrontend.services.{AuthResult, AuthService}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendHeaderCarrierProvider

import scala.concurrent.{ExecutionContext, Future}
import scala.language.higherKinds

abstract class AuthAction[P[_]](defaultParser: PlayBodyParsers, appConfig: AppConfig)
    extends ActionBuilder[P, AnyContent]
    with FrontendHeaderCarrierProvider {
  protected def redirectToLogin[A](request: Request[A]): Result =
    Redirect(
      appConfig.loginUrl,
      Map("continue" -> Seq(s"${appConfig.loginReturnBase}${request.uri}"), "origin" -> Seq(appConfig.appName)))

  override def parser: BodyParser[AnyContent] = defaultParser.defaultBodyParser
}

sealed trait Enrolment

object Enrolment {
  case object EnrolledAsOrganisation extends Enrolment
  case object NotEnrolled extends Enrolment
  case object NonOrganisationUser extends Enrolment
}

case class AuthenticatedRequest[A](request: Request[A], enrolment: Enrolment) extends WrappedRequest(request)

class AuthActionWithProfile @Inject()(defaultParser: PlayBodyParsers, authService: AuthService, appConfig: AppConfig)(
  override implicit val executionContext: ExecutionContext)
    extends AuthAction[AuthenticatedRequest](defaultParser, appConfig) {

  override def invokeBlock[A](request: Request[A], block: AuthenticatedRequest[A] => Future[Result]): Future[Result] = {
    implicit val headerCarrier: HeaderCarrier = hc(request)

    import AuthResult._

    authService.authenticate().flatMap {
      case EnrolledAsOrganisation => block(AuthenticatedRequest(request, Enrolment.EnrolledAsOrganisation))
      case NotEnrolled            => block(AuthenticatedRequest(request, Enrolment.NotEnrolled))
      case NonOrganisationUser    => block(AuthenticatedRequest(request, Enrolment.NonOrganisationUser))
      case NotLoggedIn            => Future.successful(redirectToLogin(request))
    }
  }
}

class AuthActionNoProfile @Inject()(defaultParser: PlayBodyParsers, authService: AuthService, appConfig: AppConfig)(
  override implicit val executionContext: ExecutionContext)
    extends AuthAction[Request](defaultParser, appConfig) {

  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {
    implicit val headerCarrier: HeaderCarrier = hc(request)

    authService.authenticateNoProfile().flatMap { loggedIn =>
      if (loggedIn) block(request) else Future.successful(redirectToLogin(request))
    }
  }
}
