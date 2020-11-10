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
import play.api.http.Status
import play.api.mvc.Result
import play.api.test.Helpers._
import uk.gov.hmrc.icedsubscriptionfrontend.config.MockAppConfig
import uk.gov.hmrc.icedsubscriptionfrontend.services.{AuthResult, MockAuthService}
import uk.gov.hmrc.icedsubscriptionfrontend.views.html.{AlreadyEnrolledPage, LandingPage}
import uk.gov.hmrc.play.bootstrap.tools.Stubs.stubMessagesControllerComponents

import scala.concurrent.Future

class SubscriptionControllerSpec extends SpecBase with MockAuthService with MockAppConfig {
  val appNme    = "iced-subscription-frontend"
  val returnUrl = ""

  val landingPage: LandingPage                 = app.injector.instanceOf[LandingPage]
  val alreadyEnrolledPage: AlreadyEnrolledPage = app.injector.instanceOf[AlreadyEnrolledPage]

  val authAction = new AuthAction(stubMessagesControllerComponents().parsers, mockAuthService, mockAppConfig)

  private val controller =
    new SubscriptionController(
      mockAppConfig,
      authAction,
      stubMessagesControllerComponents(),
      landingPage,
      alreadyEnrolledPage)

  class Test {
    MockAppConfig.footerLinkItems returns Nil anyNumberOfTimes ()
  }

  "GET /" should {
    "return 200" in new Test {
      val result: Future[Result] = controller.index(fakeRequest)
      status(result) shouldBe Status.OK
    }

    "return HTML for the 'Landing' page" in new Test {
      val result: Future[Result] = controller.index(fakeRequest)
      contentType(result)     shouldBe Some("text/html")
      charset(result)         shouldBe Some("utf-8")
      contentAsString(result) should include("landing.heading")
    }
  }

  "GET /start" when {
    "not authenticated" should {
      "redirect to a login page" in new Test {
        val loginUrl    = "http://loginHost:1234/sign-in"
        val continueUrl = s"$loginUrl?continue=somePath&origin=$appNme"

        MockAppConfig.loginUrl returns loginUrl
        MockAppConfig.loginReturnBase returns returnUrl
        MockAppConfig.appName returns "iced-subscription-frontend"
        MockAuthService.authenticate returns Future.successful(AuthResult.NotLoggedIn)

        val result: Future[Result] = controller.start(fakeRequest)
        redirectLocation(result) shouldBe Some(continueUrl)
      }
    }

    "authenticated without a HMRC-SS-ORG" should {
      "redirect to the eori common component frontend" in new Test {
        val eoriCommonComponentStartUrl: String = "http://localhost:1234/customs-enrolment-services/gbss/subscribe"
        MockAppConfig.eoriCommonComponentStartUrl returns eoriCommonComponentStartUrl
        MockAuthService.authenticate returns Future.successful(AuthResult.NotEnrolled)

        val result: Future[Result] = controller.start(fakeRequest)
        redirectLocation(result) shouldBe Some(eoriCommonComponentStartUrl)
      }
    }

    "authenticated with a HMRC-SS-ORG" should {
      "return HTML for the 'Already enrolled' page" in new Test {
        MockAuthService.authenticate returns Future.successful(AuthResult.Enrolled)
        val result: Future[Result] = controller.start(fakeRequest)

        contentType(result)     shouldBe Some("text/html")
        charset(result)         shouldBe Some("utf-8")
        contentAsString(result) should include("alreadyEnrolled.heading")
      }
    }

    "authenticated with but as an individual" should {
      "return HTML for the 'nonOrganisationPage' page" in new Test {
        MockAuthService.authenticate returns Future.successful(
          AuthResult.BadUserAffinity(Some(UnsupportedAffinityGroup.Individual)))
        val result: Future[Result] = controller.start(fakeRequest)

        contentType(result)     shouldBe Some("text/html")
        charset(result)         shouldBe Some("utf-8")
        contentAsString(result) should include("nonOrganisation.heading")
        contentAsString(result) should include("nonOrganisation.individual")
      }
    }

    "authenticated with but as an agent" should {
      "return HTML for the 'nonOrganisationPage' page" in new Test {
        MockAuthService.authenticate returns Future.successful(
          AuthResult.BadUserAffinity(Some(UnsupportedAffinityGroup.Agent)))
        val result: Future[Result] = controller.start(fakeRequest)

        contentType(result)     shouldBe Some("text/html")
        charset(result)         shouldBe Some("utf-8")
        contentAsString(result) should include("nonOrganisation.heading")
        contentAsString(result) should include("nonOrganisation.agent")
      }
    }
  }
}
