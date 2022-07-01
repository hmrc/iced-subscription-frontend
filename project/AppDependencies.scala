import play.core.PlayVersion
import sbt._

object AppDependencies {

  val compile = Seq(
    "uk.gov.hmrc" %% "bootstrap-frontend-play-28" % "5.25.0",
    "uk.gov.hmrc" %% "play-frontend-hmrc"         % "3.21.0-play-28",
    "uk.gov.hmrc" %% "play-language"              % "5.3.0-play-28"
  )

  val test = Seq(
    "uk.gov.hmrc"            %% "bootstrap-test-play-28" % "5.25.0",
    "org.scalamock"          %% "scalamock"              % "5.2.0",
    "org.jsoup"              % "jsoup"                   % "1.15.1",
    "com.typesafe.play"      %% "play-test"              % PlayVersion.current,
    "org.pegdown"            % "pegdown"                 % "1.6.0"
  ).map(_ % "test, it")
}
