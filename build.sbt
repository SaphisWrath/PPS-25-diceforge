val scala3Version = "3.8.4"

lazy val root = project
  .in(file("."))
  .settings(
    name := "PPS-25-diceforge",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies++=Seq(
      "org.scalatest" %% "scalatest" % "3.2.19" % Test,
      "org.scalatestplus" %% "mockito-5-23" % "3.2.20.0" % "test"
    )
  )