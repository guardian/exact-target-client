crossScalaVersions := Seq("2.8.1", "2.9.1")

libraryDependencies ++= {
    Seq(
        "joda-time" % "joda-time" % "1.6.2",
        "commons-httpclient" % "commons-httpclient" % "3.1"
    )
}

seq(sbtscalaxb.Plugin.scalaxbSettings: _*)

sourceGenerators in Compile <+= scalaxb.identity