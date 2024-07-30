import sbt.*

object AppDependencies {

  val bootstrapVersion = "9.1.0"

  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc" %% "bootstrap-frontend-play-30" % bootstrapVersion,
    "uk.gov.hmrc" %% "play-frontend-hmrc-play-30" % "10.5.0"
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"       %% "bootstrap-test-play-30" % bootstrapVersion,
    "org.scalamock"     %% "scalamock"              % "6.0.0",
    "org.jsoup"         %  "jsoup"                  % "1.18.1"
  ).map(_ % Test)

}
