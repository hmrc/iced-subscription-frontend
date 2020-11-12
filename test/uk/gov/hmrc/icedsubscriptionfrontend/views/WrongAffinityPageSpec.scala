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
import uk.gov.hmrc.icedsubscriptionfrontend.controllers.UnsupportedAffinityGroup
import uk.gov.hmrc.icedsubscriptionfrontend.controllers.UnsupportedAffinityGroup.{Agent, Individual}
import uk.gov.hmrc.icedsubscriptionfrontend.views.html.WrongAffinityPage

class WrongAffinityPageSpec extends SpecBase {

  lazy val view: WrongAffinityPage = inject[WrongAffinityPage]
  lazy val signOutAndContinueUrl: String =
    controllers.routes.SignOutController.signOut(controllers.routes.SubscriptionController.start.url).url

  object Selectors {
    val h1          = "h1"
    val h2          = "h2"
    val warningText = ".govuk-warning-text__text"
    val paragraph   = "p"
    val listItem    = "li"
    val startButton = ".govuk-button--start"
    val link        = ".govuk-link"
  }

  class Test(affinityGroup: UnsupportedAffinityGroup) {
    lazy val html: Html         = view(affinityGroup)(fakeRequest, messages, appConfig)
    lazy val document: Document = Jsoup.parse(html.toString)
  }

  "WrongAffinityPage" when {

    "logged in as an Individual" must {
      "have a only one page heading" in new Test(Individual) {
        document.select(Selectors.h1).text shouldBe "Set up a new account"
        document.select(Selectors.h1).size shouldBe 1
      }

      "have a paragraph explaining the user is logged in as an Individual" in new Test(Individual) {
        document
          .select(Selectors.paragraph)
          .first()
          .text shouldBe "You have signed in with a individual account. To enrol with safety and security you need an Organisation account."
      }

      "have a paragraph to explaining what to do next" in new Test(Individual) {
        document
          .select(Selectors.paragraph)
          .get(1)
          .text shouldBe "You need to:"
      }

      "have a list explaining what to do next" in new Test(Individual) {
        document
          .select(Selectors.paragraph)
          .get(1)
          .text shouldBe "You need to:"
        document
          .select(Selectors.listItem)
          .first
          .text shouldBe "Create an Organisation account on the Government Gateway website"
        document
          .select(Selectors.listItem)
          .get(1)
          .text shouldBe "Sign in to HMRC with the new sign in details"
        document
          .select(Selectors.listItem)
          .get(2)
          .text shouldBe "Enrol with safety and security using the new account"
      }

      "have a sign out button with the correct continue URL" in new Test(Individual) {
        document.select(Selectors.startButton).text         shouldBe "Sign out and create 'Organisation' account"
        document.select(Selectors.startButton).attr("href") shouldBe signOutAndContinueUrl
      }
    }

    "logged in as an Agent" must {
      "have a only one page heading" in new Test(Agent) {
        document.select(Selectors.h1).text shouldBe "Set up a new account"
        document.select(Selectors.h1).size shouldBe 1
      }

      "have a paragraph explaining the user is logged in as an Agent" in new Test(Agent) {
        document
          .select(Selectors.paragraph)
          .first()
          .text shouldBe "You have signed in with a agent account. To enrol with safety and security you need an Organisation account."
      }

      "have a paragraph to explaining what to do next" in new Test(Agent) {
        document
          .select(Selectors.paragraph)
          .get(1)
          .text shouldBe "You need to:"
      }

      "have a list explaining what to do next" in new Test(Agent) {
        document
          .select(Selectors.paragraph)
          .get(1)
          .text shouldBe "You need to:"
        document
          .select(Selectors.listItem)
          .first
          .text shouldBe "Create an Organisation account on the Government Gateway website"
        document
          .select(Selectors.listItem)
          .get(1)
          .text shouldBe "Sign in to HMRC with the new sign in details"
        document
          .select(Selectors.listItem)
          .get(2)
          .text shouldBe "Enrol with safety and security using the new account"
      }

      "have a sign out button with the correct continue URL" in new Test(Agent) {
        document.select(Selectors.startButton).text         shouldBe "Sign out and create 'Organisation' account"
        document.select(Selectors.startButton).attr("href") shouldBe signOutAndContinueUrl
      }
    }

  }
}
