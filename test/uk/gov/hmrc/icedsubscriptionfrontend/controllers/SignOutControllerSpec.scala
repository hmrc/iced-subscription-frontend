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
import play.api.mvc.Session
import play.api.test.Helpers._
import uk.gov.hmrc.play.bootstrap.tools.Stubs.stubMessagesControllerComponents

import scala.concurrent.Await

class SignOutControllerSpec extends SpecBase {

  private val controller = new SignOutController(stubMessagesControllerComponents())

  "GET /sign-out" when {
    "given a continue URL" should {
      val continueUrl = "/continue"

      "return 303" in {
        val result = controller.signOut(continueUrl)(fakeRequest)

        status(result) shouldBe 303
      }

      "redirect to given URL" in {
        val result = controller.signOut(continueUrl)(fakeRequest)

        redirectLocation(result) shouldBe Some(continueUrl)
      }

      "start a new session" in {
        val result = controller.signOut(continueUrl)(fakeRequest.withSession(("test", "test")))

        Await.result(result, defaultAwaitTimeout.duration).newSession shouldBe Some(Session())
      }
    }
  }
}
