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

package uk.gov.hmrc.icedsubscriptionfrontend.actions

import uk.gov.hmrc.icedsubscriptionfrontend.controllers.UnsupportedAffinityGroup
import uk.gov.hmrc.icedsubscriptionfrontend.services.AuthResult

sealed trait Enrolment

object Enrolment {

  def fromAuthResult(authResult: AuthResult): Enrolment = authResult match {
    case AuthResult.NotEnrolled            => Enrolment.NotEnrolled
    case AuthResult.EnrolledAsOrganisation => Enrolment.EnrolledAsOrganisation
    case AuthResult.BadUserAffinity(group) => Enrolment.BadUserAffinity(group)
    case _                                 => Enrolment.NonGovernmentGatewayUser
  }

  case object EnrolledAsOrganisation extends Enrolment

  case object NotEnrolled extends Enrolment

  case object NonGovernmentGatewayUser extends Enrolment

  case class BadUserAffinity(unsupportedAffinityGroup: UnsupportedAffinityGroup) extends Enrolment
}
