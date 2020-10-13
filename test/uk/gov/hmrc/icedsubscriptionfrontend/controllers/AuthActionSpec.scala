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

import base.SpecBase
import org.scalamock.handlers.CallHandler
import play.api.mvc.{Action, AnyContent}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.retrieve.EmptyRetrieval
import uk.gov.hmrc.icedsubscriptionfrontend.config.MockAppConfig
import uk.gov.hmrc.icedsubscriptionfrontend.connectors.MockAuthConnector
import uk.gov.hmrc.icedsubscriptionfrontend.services.{AuthResult, MockAuthService}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import uk.gov.hmrc.play.bootstrap.tools.Stubs.stubMessagesControllerComponents

import scala.concurrent.Future

class AuthActionSpec extends SpecBase with MockAuthService with MockAppConfig {

  val loginUrl = "someLoginUrl"

  val authAction = new AuthAction(stubMessagesControllerComponents().parsers, mockAuthService, mockAppConfig)

  class Controller extends FrontendController(stubMessagesControllerComponents()) {
    def handleRequest(): Action[AnyContent] = authAction { req =>
      if (req.enrolled) Ok("enrolled") else Ok("not enrolled")
    }
  }

  val controller = new Controller

  "AuthAction" when {
    "no active session" must {
      "redirect to login" in {
        MockAppConfig.loginUrl returns loginUrl
        MockAuthService.authenticate returns Future.successful(AuthResult.NotLoggedIn)

        val result = controller.handleRequest()(FakeRequest())

        status(result) shouldBe SEE_OTHER
        redirectLocation(result).get shouldBe loginUrl
      }
    }

    "no HMRC-SS-ORG enrolment" must {
      "call the block with an un-enrolled request" in {
        MockAuthService.authenticate returns Future.successful(AuthResult.NotEnrolled)

        val result = controller.handleRequest()(FakeRequest())

        status(result) shouldBe OK
        contentAsString(result) shouldBe "not enrolled"
      }
    }

    "active HMRC-SS-ORG enrolment found" must {
      "call the block with an enrolled request" in {
        MockAuthService.authenticate returns Future.successful(AuthResult.Enrolled)

        val result = controller.handleRequest()(FakeRequest())

        status(result) shouldBe OK
        contentAsString(result) shouldBe "enrolled"
      }
    }
  }
}
