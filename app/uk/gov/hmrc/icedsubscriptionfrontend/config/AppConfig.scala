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

package uk.gov.hmrc.icedsubscriptionfrontend.config

import javax.inject.{Inject, Singleton}
import play.api.Configuration
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

trait AppConfig {
  def appName: String
  def footerLinkItems: Seq[String]
  def loginUrl: String
  def loginReturnBase: String
  def eoriCommonComponentStartUrl: String
  def sessionTimeoutSeconds: Int
  def sessionCountdownSeconds: Int
}

@Singleton
class AppConfigImpl @Inject()(config: Configuration, servicesConfig: ServicesConfig) extends AppConfig {
  lazy val appName: String = config.getOptional[String]("appName").getOrElse("APP NAME NOT SET")

  lazy val footerLinkItems: Seq[String] = config.getOptional[Seq[String]]("footerLinkItems").getOrElse(Seq())

  lazy val loginUrl: String        = config.get[String]("login.url")
  lazy val loginReturnBase: String = config.get[String]("login.return-base")

  private val eoriCommonComponentBaseUri  = config.get[String]("eori-common-component-frontend.base")
  private val eoriCommonComponentStartUri = config.get[String]("eori-common-component-frontend.start")
  lazy val eoriCommonComponentStartUrl    = s"$eoriCommonComponentBaseUri$eoriCommonComponentStartUri"

  lazy val sessionTimeoutSeconds: Int   = config.get[Int]("session.timeoutSeconds")
  lazy val sessionCountdownSeconds: Int = config.get[Int]("session.countdownSeconds")
}
