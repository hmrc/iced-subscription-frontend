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

import javax.inject.{Inject, Singleton}
import play.api.mvc._
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import uk.gov.hmrc.icedsubscriptionfrontend.config.AppConfig
import uk.gov.hmrc.icedsubscriptionfrontend.services.{AuthResult, AuthService}
import uk.gov.hmrc.icedsubscriptionfrontend.services.AuthResult.{NotEnrolled, NotLoggedIn}
import uk.gov.hmrc.icedsubscriptionfrontend.views.html.LandingPage

import scala.concurrent.Future

@Singleton
class SubscriptionController @Inject()(
  appConfig: AppConfig,
  authAction: AuthAction,
  mcc: MessagesControllerComponents,
  landingPage: LandingPage)
    extends FrontendController(mcc) {

  implicit val config: AppConfig = appConfig

  val index: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(landingPage()))
  }

  def start: Action[AnyContent] = authAction.async { implicit request =>
    request.enrolled match {
      case true =>
        Future.successful(
          Redirect(uk.gov.hmrc.icedsubscriptionfrontend.controllers.routes.SubscriptionController.index()))
      case false => Future.successful(Redirect(appConfig.eoriCommonComponentStartUrl))
    }
  }
}
