import play.core.PlayVersion.current
import play.sbt.PlayImport._
import sbt.Keys.libraryDependencies
import sbt._

object AppDependencies {

  val compile = Seq(
    "uk.gov.hmrc" %% "bootstrap-frontend-play-27" % "2.25.0",
    "uk.gov.hmrc" %% "play-frontend-hmrc"         % "0.19.0-play-27",
    "uk.gov.hmrc" %% "play-frontend-govuk"        % "0.50.0-play-27"
  )

  val test = Seq(
    "uk.gov.hmrc"            %% "bootstrap-test-play-27"      % "2.25.0",
    "org.scalatest"          %% "scalatest"                   % "3.1.2",
    "org.scalatestplus.play" %% "scalatestplus-play"          % "3.1.2",
    "org.scalamock"          %% "scalamock-scalatest-support" % "3.6.0",
    "org.jsoup"              % "jsoup"                        % "1.10.2",
    "com.typesafe.play"      %% "play-test"                   % current,
    "org.scalatestplus.play" %% "scalatestplus-play"          % "4.0.3",
    "com.vladsch.flexmark"   % "flexmark-all"                 % "0.35.10"
  ).map(_ % "test, it")
}
