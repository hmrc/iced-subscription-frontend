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

package base

import akka.stream.Materializer
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatest.TryValues
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.play.guice._
import play.api.{Application, Environment, Mode}
import play.api.i18n.{Messages, MessagesApi}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.{AnyContentAsEmpty, MessagesControllerComponents}
import play.api.test.CSRFTokenHelper._
import play.api.test.{FakeRequest, Injecting}
import uk.gov.hmrc.http.{HeaderCarrier, SessionKeys}
import uk.gov.hmrc.icedsubscriptionfrontend.config.AppConfig

import scala.concurrent.ExecutionContext

trait SpecBase
    extends AnyWordSpecLike with Matchers
    with GuiceOneAppPerSuite
    with TryValues
    with ScalaFutures
    with IntegrationPatience
    with Injecting {

  override lazy val app: Application = new GuiceApplicationBuilder()
    .in(Environment.simple(mode = Mode.Dev))
    .configure("metrics.enabled" -> "false")
    .build()

  lazy val mcc: MessagesControllerComponents = inject[MessagesControllerComponents]

  lazy val requestPath = "somePath"
  lazy val fakeRequest: FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest("", requestPath)
      .withSession(SessionKeys.sessionId -> "foo")
      .withCSRFToken
      .asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]

  implicit lazy val appConfig: AppConfig = inject[AppConfig]

  implicit lazy val mat: Materializer    = inject[Materializer]
  implicit lazy val ec: ExecutionContext = inject[ExecutionContext]

  lazy val messagesApi: MessagesApi = inject[MessagesApi]
  implicit val messages: Messages   = messagesApi.preferred(fakeRequest)

  implicit val hc: HeaderCarrier = HeaderCarrier()
}
