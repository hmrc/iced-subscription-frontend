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
import uk.gov.hmrc.icedsubscriptionfrontend.views.html.SuccessfullyEnrolledPage

class SuccessfullyEnrolledPageSpec extends SpecBase {

  lazy val view: SuccessfullyEnrolledPage = inject[SuccessfullyEnrolledPage]

  object Selectors {
    val h1 = "h1"
    val h2 = "h2"
    val paragraph = "p"
    val listItem = "li"
    val infoSection = "#info"
    val link = ".govuk-link"
  }

  lazy val html: Html = view()(messages, FakeRequest())
  lazy val document: Document = Jsoup.parse(html.toString)

  "SuccessfullyEnrolledPage" must {

    "have the correct title" in {
      document.title shouldBe "You have successfully enrolled with S&S GB - Enrol with the Safety and Security service - GOV.UK"
    }

    "have a sign out link" in {

      val link = document.select(".hmrc-sign-out-nav__link")

      link.text shouldBe "Sign out"
      link.attr("href") shouldBe controllers.routes.SignOutController.signOut.url
    }

    "have a only one page heading" in {
      document.select(Selectors.h1).text shouldBe "You have successfully enrolled with S&S GB"
      document.select(Selectors.h1).size shouldBe 1
    }

    "have indication of when active" in {

      document.select("#main-content > div > div > p:nth-child(2)").text shouldBe
        "You can submit an Entry Summary declaration, once your account is active."
    }

    "have a paragraph explaining third party" in {

        document.select("#main-content > div > div > p:nth-child(3)")
        .text shouldBe "You will need to use third party software in order to do this."
    }


    "have information about call charges" in {
      val section = document.select(Selectors.infoSection)

      section.select(Selectors.h2).text shouldBe "If you need help"

      section
        .select(Selectors.paragraph)
        .text shouldBe "Telephone: 0300 322 7067" + " Monday to Friday, 9am to 5pm (except public holidays)"

      val link = document.select("#main-content > div > div > div > p > a")

      link.text shouldBe "Find out about call charges"
      link.attr("href") shouldBe "https://www.gov.uk/call-charges"
    }

    "have a technical issues link" in {

      val link = document.select("#main-content > div > div > a")

      link.text shouldBe "Is this page not working properly? (opens in new tab)"
      link.attr("href") should contain
      "http://localhost:9250/contact/report-technical-problem?newTab=true&service=iced-subscription-frontend&referrerUrl=%2F"
    }
  }
}
