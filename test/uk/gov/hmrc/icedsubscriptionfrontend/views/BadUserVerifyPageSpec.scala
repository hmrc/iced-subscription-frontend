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

package uk.gov.hmrc.icedsubscriptionfrontend.views

import base.SpecBase
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.test.FakeRequest
import play.twirl.api.Html
import uk.gov.hmrc.icedsubscriptionfrontend.controllers
import uk.gov.hmrc.icedsubscriptionfrontend.views.html.BadUserVerifyPage

class BadUserVerifyPageSpec extends SpecBase {

  lazy val view: BadUserVerifyPage = inject[BadUserVerifyPage]

  object Selectors {
    val h1        = "h1"
    val paragraph = "p"
    val link      = ".govuk-link"
  }

  lazy val html: Html         = view()(messages, FakeRequest())
  lazy val document: Document = Jsoup.parse(html.toString)

  "BadUserAgentPage" must {

    "have the correct title" in {
      document.title shouldBe
        "You need a Government Gateway Organisation account to enrol for S&S GB - Enrol with the Safety and Security service - GOV.UK"
    }

    "have a sign out link" in {

      val link = document.select(".hmrc-sign-out-nav__link")

      link.text shouldBe "Sign out"
      link.attr("href") shouldBe controllers.routes.SignOutController.signOut.url
    }

    "have only one page heading" in {
      document.select(Selectors.h1).text shouldBe
        "You need a Government Gateway Organisation account to enrol for S&S GB"
      document.select(Selectors.h1).size shouldBe 1
    }

    "have a paragraph explaining the user is logged in with a Verify account" in {
      document.select("#main-content > div > div > div > p:nth-child(1)").text shouldBe
        "This is because you have signed in using your GOV.UK Verify user ID."
    }

    "have a paragraph explaining what need" in {
      document.select("#main-content > div > div > div > p:nth-child(2)").text shouldBe
        "To enrol with S&S GB you need a Government Gateway account for your organisation."
    }

    "have a paragraph with a link explaining what to do if have another organisation account" in {

      document.select("#main-content > div > div > div > p:nth-child(3)").text shouldBe
        "If you have another Government Gateway user ID, return to the sign in page and enter the " +
        "Government Gateway ID for your organisation. " +
        "(You can create a Government Gateway account for your organisation if you do not have one.)"

      val link = document.select("#sign-in").get(0)

      link.text shouldBe "return to the sign in page"
      link.attr("href") shouldBe controllers.routes.SignOutController.signOutToRestart.url
    }

    "have a technical issues link" in {

      val link = document.select("#main-content > div > div > a")

      link.text shouldBe "Is this page not working properly? (opens in new tab)"
      link.attr("href") should contain
      "http://localhost:9250/contact/report-technical-problem?newTab=true&service=iced-subscription-frontend&referrerUrl=%2F"
    }
  }
}
