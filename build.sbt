releaseSettings

release-cross-build

name := "exact-target-client"

organization := "com.gu"

scalaVersion := "2.9.2"

crossScalaVersions := Seq("2.9.2", "2.9.3", "2.10.3")

resolvers += ("Sonatype Public" at "https://oss.sonatype.org/content/repositories/public/")

libraryDependencies ++= {
  Seq(
    "joda-time" % "joda-time" % "2.3",
    "org.joda" % "joda-convert" % "1.6",
    "commons-httpclient" % "commons-httpclient" % "3.1",
    "org.jdom" % "jdom" % "1.1",
    "org.slf4j" % "slf4j-api" % "1.6.1" % "provided",
    "org.mockito" % "mockito-core" % "1.9.5" % "test",
    "ch.qos.logback" % "logback-classic" % "1.0.7" % "test",
    "org.scalatest" %% "scalatest" % "1.9.1" % "test"
  )
}

publishTo <<= (version) { version: String =>
  val publishType = if (version.endsWith("SNAPSHOT")) "snapshots" else "releases"
  Some(
    Resolver.file(
      s"guardian github $publishType",
      file(s"${System.getProperty("user.home")}/guardian.github.com/maven/repo-$publishType")
    )
  )
}

publishArtifact in (Compile, packageDoc) := false

publishArtifact in (Compile, packageSrc) := true
