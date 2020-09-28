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

package uk.gov.hmrc.icedsubscriptionfrontend.views

import base.SpecBase
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.twirl.api.Html
import uk.gov.hmrc.icedsubscriptionfrontend.controllers
import uk.gov.hmrc.icedsubscriptionfrontend.views.html.LandingPage

class LandingPageSpec extends SpecBase {

  lazy val view: LandingPage = inject[LandingPage]

  object Selectors {
    val startButton = ".govuk-button--start"
  }

  lazy val html: Html         = view()(fakeRequest, messages, appConfig)
  lazy val document: Document = Jsoup.parse(html.toString)

  "LandingPage" must {
    "have a start button" in {
      document.select(Selectors.startButton).text         shouldBe "Start now"
      document.select(Selectors.startButton).attr("href") shouldBe controllers.routes.SubscriptionController.start.url
    }
  }

}
