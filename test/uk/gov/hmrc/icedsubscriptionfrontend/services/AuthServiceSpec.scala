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
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals._
import uk.gov.hmrc.auth.core.retrieve.~
import uk.gov.hmrc.auth.core.syntax.retrieved.authSyntaxForRetrieved
import uk.gov.hmrc.icedsubscriptionfrontend.connectors.MockAuthConnector

import scala.concurrent.Future

class AuthServiceSpec extends SpecBase with MockAuthConnector {

  val service = new AuthService(mockAuthConnector)

  def stubAuth(): CallHandler[Future[Enrolments ~ Option[AffinityGroup]]] =
    MockAuthConnector
      .authorise(AuthProviders(AuthProvider.GovernmentGateway), allEnrolments and affinityGroup)

  def activeEnrolment(key: String): Enrolment = Enrolment(key = key)

  private val ssEnrolment = activeEnrolment("HMRC-SS-ORG")

  private val activeSsEnrolments = Enrolments(Set(ssEnrolment))
  private val otherEnrolments    = Enrolments(Set(activeEnrolment("OTHER")))

  "AuthService.authenticate" when {
    "there is no active session" must {
      "return NotLoggedIn" in {
        stubAuth() returns Future.failed(new NoActiveSession("") {})

        service.authenticate().futureValue shouldBe AuthResult.NotLoggedIn
      }
    }

    "user is an individual (even with correct enrolment)" must {
      "return NonOrganisationUser" in {
        stubAuth() returns Future.successful(activeSsEnrolments and Some(AffinityGroup.Individual))

        service.authenticate().futureValue shouldBe AuthResult.NonOrganisationUser
      }
    }

    "user is an agent (even with correct enrolment)" must {
      "return NonOrganisationUser" in {
        stubAuth() returns Future.successful(activeSsEnrolments and Some(AffinityGroup.Agent))

        service.authenticate().futureValue shouldBe AuthResult.NonOrganisationUser
      }
    }

    "user has no affinity group (even with correct enrolment)" must {
      "return NonOrganisationUser" in {
        stubAuth() returns Future.successful(activeSsEnrolments and None)

        service.authenticate().futureValue shouldBe AuthResult.NonOrganisationUser
      }
    }

    "user is not a GGW user" must {
      "return NonOrganisationUser" in {
        stubAuth() returns Future.failed(UnsupportedAuthProvider())

        service.authenticate().futureValue shouldBe AuthResult.NonOrganisationUser
      }
    }

    "user has no enrolment" must {
      "return NotEnrolled" in {
        stubAuth() returns Future.successful(Enrolments(Set.empty) and Some(AffinityGroup.Organisation))

        service.authenticate().futureValue shouldBe AuthResult.NotEnrolled
      }
    }

    "user has a different enrolment" must {
      "return NotEnrolled" in {
        stubAuth() returns Future.successful(otherEnrolments and Some(AffinityGroup.Organisation))

        service.authenticate().futureValue shouldBe AuthResult.NotEnrolled
      }
    }

    "user HMRC-SS-ORG enrolment is not active" must {
      "return NotEnrolled" in {
        stubAuth() returns Future.successful(
          Enrolments(Set(ssEnrolment.copy(state = "disabled"))) and Some(AffinityGroup.Organisation))

        service.authenticate().futureValue shouldBe AuthResult.NotEnrolled
      }
    }

    "user HMRC-SS-ORG enrolment is active" must {
      "return EnrolledAsOrganisation" in {
        stubAuth() returns Future.successful(activeSsEnrolments and Some(AffinityGroup.Organisation))

        service.authenticate().futureValue shouldBe AuthResult.EnrolledAsOrganisation
      }
    }
  }
}
