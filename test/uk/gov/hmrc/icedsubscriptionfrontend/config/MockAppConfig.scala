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

package uk.gov.hmrc.icedsubscriptionfrontend.config

import org.scalamock.handlers.CallHandler
import org.scalamock.scalatest.MockFactory

trait MockAppConfig extends MockFactory {
  val mockAppConfig: AppConfig = mock[AppConfig]

  object MockAppConfig {
    def appName: CallHandler[String] = (mockAppConfig.appName _).expects()

    def footerLinkItems: CallHandler[Seq[String]] = (mockAppConfig.footerLinkItems _).expects()

    def loginUrl: CallHandler[String] = (mockAppConfig.loginUrl _).expects()

    def loginReturnBase: CallHandler[String] = (mockAppConfig.loginReturnBase _).expects()

    def eoriCommonComponentStartUrl: CallHandler[String] = (mockAppConfig.eoriCommonComponentStartUrl _).expects()

    def sessionTimeoutSeconds: CallHandler[Int]   = (mockAppConfig.sessionTimeoutSeconds _).expects()
    def sessionCountdownSeconds: CallHandler[Int] = (mockAppConfig.sessionCountdownSeconds _).expects()
  }
}
