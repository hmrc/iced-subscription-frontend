import sbt.*

object AppDependencies {

  val bootstrapVersion = "9.11.0"

  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc" %% "bootstrap-frontend-play-30" % bootstrapVersion,
    "uk.gov.hmrc" %% "play-frontend-hmrc-play-30" % "11.13.0"
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"       %% "bootstrap-test-play-30" % bootstrapVersion,
    "org.scalamock"     %% "scalamock"              % "6.2.0",
    "org.jsoup"         %  "jsoup"                  % "1.19.1"
  ).map(_ % Test)

}
