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
import uk.gov.hmrc.icedsubscriptionfrontend.views.html.NotFoundTemplate

class NotFoundTemplateSpec extends SpecBase {

  lazy val view: NotFoundTemplate = inject[NotFoundTemplate]

  object Selectors {
    val h1        = "h1"
    val paragraph = "p"
  }

  lazy val html: Html         = view()(messages, FakeRequest())
  lazy val document: Document = Jsoup.parse(html.toString)

  "NotFoundTemplate" must {

    "have the correct title" in {
      document.title shouldBe "Page not found"
    }

    "have correct heading" in {
      document.select(Selectors.h1).text shouldBe "Page not found"
      document.select(Selectors.h1).size shouldBe 1
    }

    "have suitable message" in {
      document.select("#main-content > div > div > div > p:nth-child(1)").text shouldBe
        "If you typed the web address, check it is correct."
      document.select("#main-content > div > div > div > p:nth-child(2)").text shouldBe
        "If you pasted the web address, check you copied the entire address."
    }

    "have a technical issues link" in {

      val link = document.select("#main-content > div > div > a")

      link.text shouldBe "Is this page not working properly? (opens in new tab)"
      link.attr("href") should contain
      "http://localhost:9250/contact/report-technical-problem?newTab=true&service=iced-subscription-frontend&referrerUrl=%2F"
    }
  }
}
