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
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.icedsubscriptionfrontend.config.AppConfig
import uk.gov.hmrc.icedsubscriptionfrontend.views.html.SuccessfullyEnrolledPage
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

@Singleton
class HandbackController @Inject()(
  appConfig: AppConfig,
  mcc: MessagesControllerComponents,
  successfullyEnrolledPage: SuccessfullyEnrolledPage)
    extends FrontendController(mcc) {
  implicit val config: AppConfig = appConfig

  def successfullyEnrolled: Action[AnyContent] = Action { implicit request =>
    Ok(successfullyEnrolledPage())
  }
}
