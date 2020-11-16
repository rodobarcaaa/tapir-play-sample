name := """tapir-play-sample"""
organization := "com.github.gaeljw"

version := "0.1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.3"

val tapirVersion = "0.17.0-M8"

libraryDependencies += guice

// Tapir
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirVersion
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-play-server" % tapirVersion

// JSON handling (you can use Circe instead of Play-JSON)
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-json-play" % tapirVersion

// Tapir OpenAPI
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % tapirVersion
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % tapirVersion

// Swagger UI for Play
// You can also host Swagger UI by yourself and get rid of this dependency
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-play" % tapirVersion

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.github.gaeljw.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.github.gaeljw.binders._"
