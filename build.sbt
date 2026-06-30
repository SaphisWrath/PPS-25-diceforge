val scala3Version = "3.8.4"

lazy val root = project
  .in(file("."))
  .settings(
    name := "PPS-25-diceforge",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies++= {
      // Determine OS version of JavaFX binaries
      lazy val osName = System.getProperty("os.name") match {
        case n if n.startsWith("Linux") => "linux"
        case n if n.startsWith("Mac") => "mac"
        case n if n.startsWith("Windows") => "win"
        case _ => throw new Exception("Unknown platform!")
      }
      Seq(
        "org.scalatest" %% "scalatest" % "3.2.19" % Test,
        "org.scalatestplus" %% "mockito-5-23" % "3.2.20.0" % "test",
        "org.openjfx" % s"javafx-base" % "16" classifier osName,
        "org.openjfx" % s"javafx-controls" % "16" classifier osName,
        "org.openjfx" % s"javafx-fxml" % "16" classifier osName,
        "org.openjfx" % s"javafx-graphics" % "16" classifier osName,
        "org.openjfx" % s"javafx-media" % "16" classifier osName,
        "org.openjfx" % s"javafx-swing" % "16" classifier osName,
        "org.openjfx" % s"javafx-web" % "16" classifier osName,
        "org.scalafx" %% "scalafx" % "16.0.0-R24"
      )
    },

    scalacOptions ++= Seq(
      "-Wconf:msg=Implicit parameters should be provided with a `using` clause:s"
    )
  )