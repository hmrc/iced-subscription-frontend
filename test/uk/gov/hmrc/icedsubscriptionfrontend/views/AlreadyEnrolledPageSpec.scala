/*
 * Copyright 2021 HM Revenue & Customs
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
import org.jsoup.nodes.{Document, Element}
import play.api.test.FakeRequest
import play.twirl.api.Html
import uk.gov.hmrc.icedsubscriptionfrontend.views.html.AlreadyEnrolledPage

class AlreadyEnrolledPageSpec extends SpecBase {

  lazy val view: AlreadyEnrolledPage = inject[AlreadyEnrolledPage]

  object Selectors {
    val h1          = "h1"
    val h2          = "h2"
    val insetText   = ".govuk-inset-text"
    val paragraph   = "p"
    val listItem    = "li"
    val startButton = ".govuk-button--start"
    val link        = ".govuk-link"
  }

  lazy val html: Html         = view()(messages, FakeRequest())
  lazy val document: Document = Jsoup.parse(html.toString)
  lazy val content: Element   = document.select("#content").first

  "AlreadyEnrolledPage" must {

    "have the correct title" in {
      document.title shouldBe "Enrol with the Safety and Security service - GOV.UK"
    }

    "have a sign out link" in {
      val link = document
        .select(Selectors.link)
        .first()

      link.text         shouldBe "Sign out"
      link.attr("href") shouldBe "/safety-and-security-subscription/sign-out"
    }

    "have a only one page heading" in {
      document.select(Selectors.h1).text shouldBe "Enrol with the Safety and Security service"
      document.select(Selectors.h1).size shouldBe 1
    }

    "have inset text for with notice" in {
      content
        .select(Selectors.paragraph)
        .first
        .text shouldBe "The GB EORI you supplied is already enrolled with the Safety and Security service."
    }

    "have a link to make a declaration" in {
      val link = content
        .select(Selectors.link)
        .first

      link.text         shouldBe "Make an Entry Summary declaration"
      link.attr("href") shouldBe "https://www.gov.uk/guidance/making-an-entry-summary-declaration"
    }
  }
}
