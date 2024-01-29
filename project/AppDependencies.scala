import play.core.PlayVersion
import sbt._

object AppDependencies {

  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc" %% "bootstrap-frontend-play-28" % "7.22.0",
    "uk.gov.hmrc" %% "play-frontend-hmrc-play-28" % "8.4.0",
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"       %% "bootstrap-test-play-28" % "7.22.0",
    "org.scalamock"     %% "scalamock"              % "5.2.0",
    "org.jsoup"         %  "jsoup"                  % "1.16.1",
    "com.typesafe.play" %% "play-test"              % PlayVersion.current
  ).map(_ % "test, it")

  val itDependencies: Seq[ModuleID] = Seq()
}
