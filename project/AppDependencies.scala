import sbt._

object AppDependencies {

  val compile = Seq(
    "uk.gov.hmrc" %% "bootstrap-frontend-play-27" % "5.2.0",
    "uk.gov.hmrc" %% "play-frontend-hmrc"         % "0.60.0-play-27",
    "uk.gov.hmrc" %% "play-language"              % "4.12.0-play-27"
  )

  val test = Seq(
    "uk.gov.hmrc"            %% "bootstrap-test-play-27" % "5.2.0",
    "org.scalamock"          %% "scalamock"              % "5.1.0",
    "org.jsoup"              % "jsoup"                   % "1.13.1",
    "com.typesafe.play"      %% "play-test"              % "2.7.9",
    "org.scalatestplus.play" %% "scalatestplus-play"     % "4.0.3",
    "com.vladsch.flexmark"   % "flexmark-all"            % "0.35.10",
    "org.pegdown"            % "pegdown"                 % "1.6.0"
  ).map(_ % "test, it")
}
