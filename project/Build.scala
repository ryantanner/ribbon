import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {
	val appName    = "ribbon"
	val appVersion = "1.0"

	val appDependencies = Seq(
    jdbc,
    anorm,
    "mysql"                          %  "mysql-connector-java"        % "5.1.25",
    "com.ibm.icu"                    %  "icu4j"                       % "51.1",
    "com.typesafe.play"              %% "play-slick"                  % "0.5.0.8",
    "com.github.tototoshi"           %% "slick-joda-mapper"           % "0.4.0",
    //"joda-time"                      % "joda-time"                    % "2.3",
    "org.scalatest"                  %% "scalatest"                   % "1.9.1"    % "test",
    "org.seleniumhq.selenium"        %  "selenium-java"               % "2.32.0"   % "test"
  )

	val main = play.Project(appName, appVersion, appDependencies).settings(
		 scalaVersion := "2.10.2",
		 scalacOptions ++= Seq(
			 "-deprecation",
			 "-feature",
			 "-unchecked",
			 "-Xfatal-warnings",
			 "-Xlint",
			 "-Yno-adapted-args",
			 "-Ywarn-all",
			 "-Ywarn-dead-code"
		),
		templatesImport ++= Seq("helpers._", "tags._", "util._"),
		testOptions in Test := Nil // Disables built-in specs2, it conflicts with ScalaTest
  )
}
