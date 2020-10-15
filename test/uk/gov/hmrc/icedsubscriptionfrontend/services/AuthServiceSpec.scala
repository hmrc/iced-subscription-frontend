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

package uk.gov.hmrc.icedsubscriptionfrontend.services

import base.SpecBase
import org.scalamock.handlers.CallHandler
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.retrieve.EmptyRetrieval
import uk.gov.hmrc.icedsubscriptionfrontend.connectors.MockAuthConnector

import scala.concurrent.Future

class AuthServiceSpec extends SpecBase with MockAuthConnector {

  val service = new AuthService(mockAuthConnector)

  def stubAuth(): CallHandler[Future[Unit]] =
    MockAuthConnector
      .authorise(Enrolment("HMRC-SS-ORG") and AuthProviders(AuthProvider.GovernmentGateway), EmptyRetrieval)

  "AuthService.authenticate" when {
    "no active session" must {
      "return NotLoggedIn" in {
        stubAuth() returns Future.failed(new NoActiveSession("") {})

        service.authenticate().futureValue shouldBe AuthResult.NotLoggedIn
      }
    }

    "no HMRC-SS-ORG enrolment" must {
      "return NotEnrolled" in {
        stubAuth() returns Future.failed(InsufficientEnrolments("HMRC-SS-ORG"))

        service.authenticate().futureValue shouldBe AuthResult.NotEnrolled
      }
    }

    "active HMRC-SS-ORG enrolment found" must {
      "return Enrolled" in {
        stubAuth() returns Future.unit

        service.authenticate().futureValue shouldBe AuthResult.Enrolled
      }
    }
  }
}
