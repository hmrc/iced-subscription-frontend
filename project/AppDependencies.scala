import play.core.PlayVersion
import sbt._

object AppDependencies {

  val compile = Seq(
    "uk.gov.hmrc" %% "bootstrap-frontend-play-28" % "5.25.0",
    "uk.gov.hmrc" %% "play-frontend-hmrc"         % "4.1.0-play-28",
    "uk.gov.hmrc" %% "play-language"              % "6.1.0-play-28"
  )

  val test = Seq(
    "uk.gov.hmrc"       %% "bootstrap-test-play-28" % "5.25.0",
    "org.scalamock"     %% "scalamock"              % "5.2.0",
    "org.jsoup"         % "jsoup"                   % "1.15.3",
    "com.typesafe.play" %% "play-test"              % PlayVersion.current
  ).map(_ % "test, it")
}
