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
import play.api.i18n.I18nSupport
import play.api.mvc._
import uk.gov.hmrc.icedsubscriptionfrontend.actions.{AuthActionWithProfile, Enrolment}
import uk.gov.hmrc.icedsubscriptionfrontend.config.AppConfig
import uk.gov.hmrc.icedsubscriptionfrontend.views.html.{AlreadyEnrolledPage, LandingPage, NonOrgGgwPage}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

@Singleton
class SubscriptionController @Inject()(
  appConfig: AppConfig,
  authAction: AuthActionWithProfile,
  mcc: MessagesControllerComponents,
  landingPage: LandingPage,
  alreadyEnrolledPage: AlreadyEnrolledPage,
  nonOrgGgwPage: NonOrgGgwPage)
    extends FrontendController(mcc)
    with I18nSupport {

  implicit val config: AppConfig = appConfig

  val index: Action[AnyContent] = Action { implicit request =>
    Ok(landingPage())
  }

  def start: Action[AnyContent] = authAction { implicit request =>
    request.enrolment match {
      case Enrolment.EnrolledAsOrganisation => Ok(alreadyEnrolledPage())
      case Enrolment.NotEnrolled            => Redirect(appConfig.eoriCommonComponentStartUrl)
      case Enrolment.NonOrganisationUser    => Ok(nonOrgGgwPage())
    }
  }
}
