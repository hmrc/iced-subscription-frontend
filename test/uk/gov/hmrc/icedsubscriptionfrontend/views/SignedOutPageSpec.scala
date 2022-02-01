/*
 * Copyright 2022 HM Revenue & Customs
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
import play.api.test.FakeRequest
import play.twirl.api.Html
import uk.gov.hmrc.icedsubscriptionfrontend.controllers
import uk.gov.hmrc.icedsubscriptionfrontend.views.html.SignedOutPage

class SignedOutPageSpec extends SpecBase {

  lazy val view: SignedOutPage = inject[SignedOutPage]
  object Selectors {
    val h1        = "h1"
    val button    = ".govuk-button"
    val paragraph = "p"
  }

  lazy val html: Html         = view()(messages, FakeRequest())
  lazy val document: Document = Jsoup.parse(html.toString)

  "have the correct title" in {
    document.title shouldBe "For your security, we signed you out - Enrol with the Safety and Security service - GOV.UK"
  }

  "have a only one page heading" in {
    document.select(Selectors.h1).text shouldBe "For your security, we signed you out"
    document.select(Selectors.h1).size shouldBe 1
  }

  "have a sign in button with the correct URL" in {
    document.select(".govuk-button").text shouldBe "Sign in"
    document.select(".govuk-button").attr("href") shouldBe controllers.routes.SubscriptionController.start.url
  }

  "indicate that answers were not saved" in {

    document.select("#main-content > div > div > p").text shouldBe
      "We did not save your answers."
  }

  "have a technical issues link" in {

    val link = document.select("#main-content > div > div > a")

    link.text shouldBe "Is this page not working properly? (opens in new tab)"
    link.attr("href") should contain
    "http://localhost:9250/contact/report-technical-problem?newTab=true&service=iced-subscription-frontend&referrerUrl=%2F"
  }
}
