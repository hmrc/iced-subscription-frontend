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
import org.jsoup.nodes.{Document, Element}
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
  lazy val content: Element   = document.select("#content").first

  // According to https://design.tax.service.gov.uk/hmrc-design-patterns/page-not-found/
  "NotFoundTemplate" must {

    "have the correct title" in {
      document.title shouldBe "Page not found"
    }

    "have correct heading" in {
      document.select(Selectors.h1).text shouldBe "Page not found"
      document.select(Selectors.h1).size shouldBe 1
    }

    "have suitable message" in {
      content.select(Selectors.paragraph).first().text shouldBe
        "If you typed the web address, check it is correct."
      content.select(Selectors.paragraph).get(1).text shouldBe
        "If you pasted the web address, check you copied the entire address."
    }
  }
}
