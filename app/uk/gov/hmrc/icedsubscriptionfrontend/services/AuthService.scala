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

package uk.gov.hmrc.icedsubscriptionfrontend.services

import javax.inject.Inject
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

sealed trait AuthResult

object AuthResult {

  case object NotLoggedIn extends AuthResult

  case object NotEnrolled extends AuthResult

  case object Enrolled extends AuthResult

}

class AuthService @Inject()(
                             val authConnector: AuthConnector
                           )(implicit ec: ExecutionContext) extends AuthorisedFunctions {

  def authenticate()(implicit hc: HeaderCarrier): Future[AuthResult] = {
    authorised(Enrolment("HMRC-SS-ORG") and AuthProviders(AuthProvider.GovernmentGateway)) {
      Future.successful(AuthResult.Enrolled)
    }.recover {
      case _: InsufficientEnrolments => AuthResult.NotEnrolled
      case _: NoActiveSession => AuthResult.NotLoggedIn
    }
  }
}
