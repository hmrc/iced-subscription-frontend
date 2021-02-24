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

package uk.gov.hmrc.icedsubscriptionfrontend.controllers

import base.SpecBase
import play.api.mvc.Cookie
import play.api.test.{DefaultAwaitTimeout, FakeRequest}
import play.api.test.Helpers.cookies
import uk.gov.hmrc.play.bootstrap.tools.Stubs.stubMessagesControllerComponents
import uk.gov.hmrc.play.language.LanguageUtils

class LanguageSwitchControllerSpec extends SpecBase with DefaultAwaitTimeout {

  val utils = inject[LanguageUtils]
  val controller = new LanguageSwitchController(utils, stubMessagesControllerComponents())

  "Hitting language selection endpoint" must {
    "redirect to Welsh translated start page if Welsh language is selected" in {
      val request = FakeRequest()
      val result = controller.switchToLanguage("cy")(request)

      cookies(result).get("PLAY_LANG") shouldBe Some(Cookie("PLAY_LANG","cy",None,"/",None,false,false,None))
    }

    "redirect to English translated start page if English language is selected" in {
      val request = FakeRequest()
      val result = controller.switchToLanguage("em")(request)

      cookies(result).get("PLAY_LANG") shouldBe Some(Cookie("PLAY_LANG","en",None,"/",None,false,false,None))
    }
  }

}
