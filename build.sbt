import sbt.Keys.packageOptions

val tapirVersion = "0.17.15"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := """tapir-play-sample""",
    organization := "com.github.rodobarcaaa",
    version := (version in ThisBuild).value,
    packageOptions += Package.ManifestAttributes("Implementation-Version" -> (version in ThisBuild).value),
    scalaVersion := "2.13.5",
    libraryDependencies ++= Seq(
      guice,
      ws,
      "com.softwaremill.sttp.tapir" %% "tapir-core"               % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-play-server"        % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-play"          % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"       % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-play"    % tapirVersion,
      "net.codingwell"              %% "scala-guice"              % "4.2.11",
      "org.scalatestplus.play"      %% "scalatestplus-play"       % "5.1.0" % Test
    ),
    publishArtifact := false
  )

// COMMANDS ALIASES
addCommandAlias("t", "test")
addCommandAlias("to", "testOnly")
addCommandAlias("tq", "testQuick")
addCommandAlias("tsf", "testShowFailed")

addCommandAlias("c", "compile")
addCommandAlias("tc", "test:compile")

addCommandAlias("f", "scalafmt")             // Format production files according to ScalaFmt
addCommandAlias("fc", "scalafmtCheck")       // Check if production files are formatted according to ScalaFmt
addCommandAlias("tf", "test:scalafmt")       // Format test files according to ScalaFmt
addCommandAlias("tfc", "test:scalafmtCheck") // Check if test files are formatted according to ScalaFmt
addCommandAlias("fmt", ";f;tf")              // Format all files according to ScalaFmt

// All the needed tasks before pushing to the repository (compile, compile test, format check in prod and test)
addCommandAlias("prep", ";c;tc;test")
addCommandAlias("build", ";c;tc")
