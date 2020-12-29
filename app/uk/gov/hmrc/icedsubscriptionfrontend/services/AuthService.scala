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

import com.google.inject.{Inject, Singleton}
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.authorise.EmptyPredicate
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals._
import uk.gov.hmrc.auth.core.retrieve.{EmptyRetrieval, ~}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.icedsubscriptionfrontend.controllers.UnsupportedAffinityGroup

import scala.concurrent.{ExecutionContext, Future}

sealed trait AuthResult

object AuthResult {
  case object NotLoggedIn extends AuthResult

  case object NotEnrolled extends AuthResult

  case object EnrolledAsOrganisation extends AuthResult

  case object NonGovernmentGatewayUser extends AuthResult

  case class BadUserAffinity(unsupportedAffinityGroup: UnsupportedAffinityGroup) extends AuthResult
}

@Singleton
class AuthService @Inject()(val authConnector: AuthConnector)(implicit ec: ExecutionContext)
    extends AuthorisedFunctions {

  // Note: the logic is primarily implemented using retrievals rather than lists of
  // predicates so that we can closely control the order of checks rather than
  // relying on any correspondence between predicate order and
  // the exception that is thrown in a particular scenario...
  def authenticate()(implicit hc: HeaderCarrier): Future[AuthResult] =
    authorised(AuthProviders(AuthProvider.GovernmentGateway))
      .retrieve(allEnrolments and affinityGroup) {
        case enrolments ~ Some(AffinityGroup.Organisation) if hasActiveEnrolment(enrolments) =>
          Future.successful(AuthResult.EnrolledAsOrganisation)
        case _ ~ Some(AffinityGroup.Organisation) =>
          Future.successful(AuthResult.NotEnrolled)
        case _ ~ Some(AffinityGroup.Individual) =>
          Future.successful(AuthResult.BadUserAffinity(UnsupportedAffinityGroup.Individual))
        case _ ~ Some(AffinityGroup.Agent) =>
          Future.successful(AuthResult.BadUserAffinity(UnsupportedAffinityGroup.Agent))
        case _ =>
          Future.successful(AuthResult.NonGovernmentGatewayUser)
      }
      .recover {
        case _: NoActiveSession         => AuthResult.NotLoggedIn
        case _: UnsupportedAuthProvider => AuthResult.NonGovernmentGatewayUser
      }

  private def hasActiveEnrolment(enrolments: Enrolments) =
    enrolments.getEnrolment("HMRC-SS-ORG").exists(_.isActivated)

  def authenticateNoProfile()(implicit hc: HeaderCarrier): Future[Boolean] =
    authConnector
      .authorise(EmptyPredicate, EmptyRetrieval)
      .map(_ => true)
      .recover {
        case _: NoActiveSession => false
      }
}
