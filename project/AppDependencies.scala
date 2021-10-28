import play.core.PlayVersion
import sbt._

object AppDependencies {

  val compile = Seq(
    "uk.gov.hmrc" %% "bootstrap-frontend-play-28" % "5.16.0",
    "uk.gov.hmrc" %% "play-frontend-hmrc"         % "1.22.0-play-28",
    "uk.gov.hmrc" %% "play-language"              % "5.1.0-play-28"
  )

  val test = Seq(
    "uk.gov.hmrc"            %% "bootstrap-test-play-28" % "5.16.0",
    "org.scalamock"          %% "scalamock"              % "5.1.0",
    "org.jsoup"              % "jsoup"                   % "1.14.3",
    "com.typesafe.play"      %% "play-test"              % PlayVersion.current,
    "org.pegdown"            % "pegdown"                 % "1.6.0"
  ).map(_ % "test, it")
}
