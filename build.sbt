val Http4sVersion = "0.21.4"

lazy val root = (project in file("."))
  .settings(
    organization := "io.andrewsmith",
    name := "voice-server",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.2",
    libraryDependencies ++= Seq(
      "org.http4s"      %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"      %% "http4s-blaze-client" % Http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
      "ch.qos.logback"  %  "logback-classic"     % "1.2.3"
    )
  )

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Xfatal-warnings",
)
