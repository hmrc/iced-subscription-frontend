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
import play.api.{Configuration, Environment}
import uk.gov.hmrc.icedsubscriptionfrontend.config.MockAppConfig
import uk.gov.hmrc.icedsubscriptionfrontend.views.html.LandingPage
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.play.bootstrap.tools.Stubs.stubMessagesControllerComponents

import scala.concurrent.Future

class SubscriptionControllerSpec extends SpecBase with MockAppConfig {
  private val env           = Environment.simple()
  private val configuration = Configuration.load(env)

  private val serviceConfig = new ServicesConfig(configuration)

  val landingPage: LandingPage = app.injector.instanceOf[LandingPage]

  private val controller = new SubscriptionController(mockAppConfig, stubMessagesControllerComponents(), landingPage)

  class Test {
    MockAppConfig.footerLinkItems returns Nil anyNumberOfTimes()
  }

  "GET /" should {
    "return 200" in new Test {
      val result: Future[Result] = controller.index(fakeRequest)
      status(result) shouldBe Status.OK
    }

    "return HTML" in new Test {
      val result: Future[Result] = controller.index(fakeRequest)
      contentType(result) shouldBe Some("text/html")
      charset(result)     shouldBe Some("utf-8")
    }
  }

  "GET /start" when {
    "not authenticated" should {
      "redirect to a login page" in new Test {
        val loginUrl = "http://loginHost:1234/sign-in"
        MockAppConfig.loginUrl returns loginUrl

        val result: Future[Result] = controller.start(fakeRequest)
        redirectLocation(result) shouldBe Some(loginUrl)
      }
    }
  }
}
