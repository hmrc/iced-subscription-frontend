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
import uk.gov.hmrc.icedsubscriptionfrontend.controllers
import uk.gov.hmrc.icedsubscriptionfrontend.views.html.LandingPage

class LandingPageSpec extends SpecBase {

  lazy val view: LandingPage = inject[LandingPage]

  object Selectors {
    val h1                  = "h1"
    val h2                  = "h2"
    val warningText         = ".govuk-warning-text__text"
    val paragraph           = "p"
    val listItem            = "li"
    val startButton         = ".govuk-button--start"
    val link                = ".govuk-link"
    val cspsSection         = "#csps"
    val requirementsSection = "#requirements"
    val waitSection         = "#wait"
  }

  lazy val html: Html         = view()(messages, FakeRequest())
  lazy val document: Document = Jsoup.parse(html.toString)
  lazy val content: Element   = document.select("#content").first

  "LandingPage" must {

    "have the correct title" in {
      document.title shouldBe "Enrol with the Safety and Security service - GOV.UK"
    }

    "have a only one page heading" in {
      document.select(Selectors.h1).text shouldBe "Enrol with the Safety and Security service"
      document.select(Selectors.h1).size shouldBe 1
    }

    "have warning text for Traders" in {
      content
        .select(Selectors.warningText)
        .first
        .text shouldBe "Warning From 1 January 2021 traders who submit Entry Summary declarations (sometimes called an ENS) " +
        "using third party software will need to enrol with the Safety and Security (S&S GB) service."
    }

    "have a paragraph explaining S&S" in {
      content
        .select(Selectors.paragraph)
        .get(0)
        .text shouldBe "S&S GB handles digital communications between customs administrators and carriers " +
        "or their appointed representatives."
    }

    "have a list of what S&S incorporates" in {
      content
        .select(Selectors.paragraph)
        .get(1)
        .text shouldBe "S&S GB incorporates the:"
      content
        .select(Selectors.listItem)
        .first
        .text shouldBe "lodging, handling and processing of an Entry Summary declaration in advance of the " +
        "arrival of goods into the UK from outside the UK"
      content
        .select(Selectors.listItem)
        .get(1)
        .text shouldBe "issuing of a Movement Reference Number (MRN)"
    }

    "have warning text for CSPs" in {
      val section = content.select(Selectors.cspsSection)

      section.select(Selectors.h2).text shouldBe "Community System Providers"

      section
        .select(Selectors.paragraph)
        .get(0)
        .text shouldBe "If you are a Community System Provider you don’t need to enrol with S&S GB to submit an ENS."
    }

    "a heading about before you enrol" in {
      val section = content.select(Selectors.requirementsSection)
      section
        .select(Selectors.h2)
        .get(0)
        .text shouldBe "Before you enrol"
    }

    "have a list of what you need to enrol" in {
      val section = content.select(Selectors.requirementsSection)

      section
        .select(Selectors.paragraph)
        .get(0)
        .text shouldBe "You’ll need:"

      val bullets = section
        .select(Selectors.listItem)

      bullets.size shouldBe 6

      bullets
        .get(0)
        .text shouldBe "a user ID and password for a Government Gateway Organisation account " +
        "(opens in new tab) - if you do not have a user ID, you can create one when you apply"

      bullets
        .get(1)
        .text shouldBe "an EORI number that starts with GB - apply for a new one (opens in new tab) " +
        "if yours does not start with GB"

      bullets
        .get(2)
        .text shouldBe "trader name"

      bullets
        .get(3)
        .text shouldBe "business UTR"

      bullets
        .get(4)
        .text shouldBe "business date of establishment"

      bullets
        .get(5)
        .text shouldBe "business address"
    }

    "have a link to GGW" in {
      val section = content.select(Selectors.requirementsSection)
      val link    = section.select(Selectors.link).first

      link.text shouldBe
        "user ID and password for a Government Gateway Organisation account (opens in new tab)"

      link.attr("href") shouldBe
        "https://www.gov.uk/log-in-register-hmrc-online-services/register"
    }

    "have a link to get an EORI" in {
      val section = content.select(Selectors.requirementsSection)
      val link    = section.select(Selectors.link).get(1)

      link.text shouldBe
        "apply for a new one (opens in new tab)"

      link
        .attr("href") shouldBe
        "https://www.gov.uk/eori?step-by-step-nav=8a543f4b-afb7-4591-bbfc-2eec52ab96c2"
    }

    "have a paragraph showing how long it takes to enrol" in {
      val section = content.select(Selectors.waitSection)

      section
        .select(Selectors.paragraph)
        .get(0)
        .text shouldBe "It takes around 5-10 minutes to apply to enrol with S&S GB. It may take 2 hours to get a decision."
    }

    "have a start button" in {
      content.select(Selectors.startButton).text shouldBe "Start now"
      content.select(Selectors.startButton).attr("href") shouldBe
        controllers.routes.SubscriptionController.start().url
    }
  }
}
