name := "exact-target-client"

organization := "com.gu"

version := "2.16-SNAPSHOT"

crossScalaVersions := Seq("2.9.0-1", "2.9.1", "2.9.2")

resolvers += ("Sonatype Public" at "https://oss.sonatype.org/content/repositories/public/")

libraryDependencies ++= {
    Seq(
        "joda-time" % "joda-time" % "2.1" % "provided",
        "commons-httpclient" % "commons-httpclient" % "3.1",
        "org.slf4j" % "slf4j-api" % "1.6.1" % "provided",
        "ch.qos.logback" % "logback-classic" % "1.0.7" % "test",
        "org.scalatest" %% "scalatest" % "1.9.1" % "test"
    )
}

libraryDependencies += "org.jdom" % "jdom" % "1.1"

libraryDependencies += "org.mockito" % "mockito-core" % "1.9.5" % "test"

publishTo <<= (version) { version: String =>
  val nexus = "http://nexus.gudev.gnl:8081/nexus/content/repositories/"
  if (version.trim.endsWith("SNAPSHOT")) Some("Guardian Internal Snapshots" at nexus + "snapshots/")
  else                                   Some("Guardian Internal Releases"  at nexus + "releases/")
}

credentials += Credentials("Sonatype Nexus Repository Manager", "nexus.gudev.gnl", "deployment", "d3pl0y")

publishArtifact in (Compile, packageDoc) := false

publishArtifact in (Compile, packageSrc) := true
