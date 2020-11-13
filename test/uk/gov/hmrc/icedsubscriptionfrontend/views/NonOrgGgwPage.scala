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
import uk.gov.hmrc.icedsubscriptionfrontend.views.html.NonOrgGgwPage

class NonOrgGgwPageSpec extends SpecBase {

  lazy val view: NonOrgGgwPage = inject[NonOrgGgwPage]
  lazy val signOutAndContinueUrl: String =
    controllers.routes.SignOutController.signOut(Some(controllers.routes.SubscriptionController.start.url)).url

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

  "NonOrgGgwPage" must {
    "have a sign out link" in {
      val link = document
        .select(Selectors.link)
        .first()

      link.text         shouldBe "Sign out"
      link.attr("href") shouldBe "/safety-and-security-subscription/sign-out"
    }

    "have a only one page heading" in {
      document.select(Selectors.h1).text shouldBe "Use a Government Gateway Organisation account"
      document.select(Selectors.h1).size shouldBe 1
    }

    "have a paragraph explaining the user is logged in to an invalid account" in {
      document
        .select(Selectors.paragraph)
        .first()
        .text shouldBe "You have signed in with the wrong type of account. To enrol with safety and security you need a Government Gateway organisation account."
    }

    "have a list explaining what to do next" when {
      "the user does not an organisation GGW account" in {
        document
          .select(Selectors.paragraph)
          .get(1)
          .text shouldBe "If you don't have an organisation account, you need to:"
        document
          .select(Selectors.listItem)
          .first
          .text shouldBe "Create an organisation account on the Government Gateway website"
        document
          .select(Selectors.listItem)
          .get(1)
          .text shouldBe "Sign in to HMRC with the new sign in details"
        document
          .select(Selectors.listItem)
          .get(2)
          .text shouldBe "Enrol with safety and security using the new account"
      }

      "user has an organisation GGW account" in {
        document
          .select(Selectors.paragraph)
          .get(2)
          .text shouldBe "If you have an organisation account, you need to:"
        document
          .select(Selectors.listItem)
          .get(3)
          .text shouldBe "Sign in to HMRC with the organisation's details"
        document
          .select(Selectors.listItem)
          .get(4)
          .text shouldBe "Enrol with safety and security using that account"
      }
    }

    "have a sign out button with the correct continue URL" in {
      document.select(Selectors.startButton).text         shouldBe "Sign out and go to Government Gateway"
      document.select(Selectors.startButton).attr("href") shouldBe signOutAndContinueUrl
    }
  }
}
