/*
 * Copyright 2023 HM Revenue & Customs
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

package uk.gov.hmrc.icedsubscriptionfrontend.config

import base.SpecBase
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.Application
import play.api.i18n.MessagesApi
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsString, _}

import scala.concurrent.Future

class ErrorHandlerSpec extends SpecBase {

  override lazy val app: Application = new GuiceApplicationBuilder()
    .overrides(bind[MessagesApi].toInstance(stubMessagesApi()))
    .build()

  val errorHandler: ErrorHandler = inject[ErrorHandler]

  "ErrorHandler" when {
    "404" must {
      "show the custom not found page" in {
        val result: Future[Result] = errorHandler.onClientError(FakeRequest(), NOT_FOUND)
        status(result) shouldBe NOT_FOUND

        lazy val document: Document = Jsoup.parse(contentAsString(result))
        // These are not present on the default template
        document.select("#main-content > div > div > div > p:nth-child(1)").text shouldBe messages("global.error.pageNotFound404.message.p1")
        document.select("#main-content > div > div > div > p:nth-child(2)").text shouldBe messages("global.error.pageNotFound404.message.p2")
      }
    }
  }

}
