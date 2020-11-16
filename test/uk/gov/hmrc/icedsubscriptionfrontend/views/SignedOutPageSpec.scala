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
import uk.gov.hmrc.icedsubscriptionfrontend.views.html.SignedOutPage

class SignedOutPageSpec extends SpecBase {

  lazy val view: SignedOutPage = inject[SignedOutPage]
  object Selectors {
    val h1     = "h1"
    val button = ".govuk-button"
  }
  lazy val html: Html         = view()(messages, appConfig)
  lazy val document: Document = Jsoup.parse(html.toString)

  "have a only one page heading" in {
    document.select(Selectors.h1).text shouldBe "For your security, we signed you out"
    document.select(Selectors.h1).size shouldBe 1
  }

  "have a sign in button with the correct URL" in {
    document.select(Selectors.button).text         shouldBe "Sign in"
    document.select(Selectors.button).attr("href") shouldBe controllers.routes.SubscriptionController.start.url
  }
}
