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
    val h1          = "h1"
    val h2          = "h2"
    val warningText = ".govuk-warning-text__text"
    val paragraph   = "p"
    val listItem    = "li"
    val startButton = ".govuk-button--start"
    val link        = ".govuk-link"
  }

  lazy val html: Html         = view()(fakeRequest, messages, appConfig)
  lazy val document: Document = Jsoup.parse(html.toString)

  "LandingPage" must {

    "have a only one page heading" in {
      document.select(Selectors.h1).text shouldBe "Enrol with the safety & security service"
      document.select(Selectors.h1).size shouldBe 1
    }

    "have warning text for Traders" in {
      document
        .select(Selectors.warningText)
        .first
        .text shouldBe "Warning From 1 January 2021 Traders who submit Entry Summary Declarations (ENS) using third party software will need to enrol with the Safety & Security (S&S) service."
    }

    "have a paragraph explaining S&S" in {
      document
        .select(Selectors.paragraph)
        .get(1)
        .text shouldBe "S&S handles digital communications between customs administrators and carriers or their appointed representatives."
    }

    "have a list of what S&S incorporates" in {
      document
        .select(Selectors.paragraph)
        .get(2)
        .text shouldBe "S&S incorporates the:"
      document
        .select(Selectors.listItem)
        .first
        .text shouldBe "lodging, handling and processing of an ENS in advance of the arrival of goods into the UK from outside the UK"
      document
        .select(Selectors.listItem)
        .get(1)
        .text shouldBe "issuing of a Movement Reference Number (MRN)"
    }

    "have warning text for CSPs" in {
      document
        .select(Selectors.warningText)
        .get(1)
        .text shouldBe "Warning Community System Providers If you are a Community System Provider you don’t need to enrol with S&S to submit an ENS."
    }

    "a heading about before you enrol" in {
      document.select(Selectors.h2).first.text shouldBe "Before you enrol"
    }

    "have a list of what you need to enrol" in {
      document
        .select(Selectors.paragraph)
        .get(4)
        .text shouldBe "You’ll need:"
      document
        .select(Selectors.listItem)
        .get(2)
        .text shouldBe "a Government Gateway user ID and password - if you do not have a user ID, you can create one when you apply"
      document
        .select(Selectors.listItem)
        .get(3)
        .text shouldBe "an EORI number that starts with GB - apply for a new one if yours does not start with GB"
    }

    "have a link to GGW" in {
      document.select(Selectors.link).first.text shouldBe "Government Gateway user ID and password"
      document
        .select(Selectors.link)
        .first
        .attr("href") shouldBe "http://localhost:9949/auth-login-stub/gg-sign-in"
    }

    "have a link to get an EORI" in {
      document.select(Selectors.link).get(1).text shouldBe "apply for a new one"
      document
        .select(Selectors.link)
        .get(1)
        .attr("href") shouldBe "https://www.gov.uk/eori?step-by-step-nav=8a543f4b-afb7-4591-bbfc-2eec52ab96c2"
    }

    "have a paragraph showing how long it takes to enrol" in {
      document
        .select(Selectors.paragraph)
        .get(5)
        .text shouldBe "It takes around n minutes to apply to enrol with S&S. You should get a decision immediately."
    }

    "have a start button" in {
      document.select(Selectors.startButton).text         shouldBe "Start now"
      document.select(Selectors.startButton).attr("href") shouldBe controllers.routes.SubscriptionController.start.url
    }

    "have a link to not enroll" in {
      document.select(Selectors.link).get(2).text         shouldBe "I don’t need to enrol with S&S"
      document.select(Selectors.link).get(2).attr("href") shouldBe "#"
    }
  }
}
