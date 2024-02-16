import play.core.PlayVersion
import sbt._

object AppDependencies {

  val bootstrapVersion = "8.4.0"

  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc" %% "bootstrap-frontend-play-30" % bootstrapVersion,
    "uk.gov.hmrc" %% "play-frontend-hmrc-play-30" % "8.5.0",
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"       %% "bootstrap-test-play-30" % bootstrapVersion,
    "org.scalamock"     %% "scalamock"              % "5.2.0",
    "org.jsoup"         %  "jsoup"                  % "1.16.1"
//    "com.typesafe.play" %% "play-test"              % PlayVersion.current
  ).map(_ % Test)

  val itDependencies: Seq[ModuleID] = Seq()
}
