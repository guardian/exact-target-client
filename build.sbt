name := "exact-target-client"

organization := "com.gu"

version := "2.5-SNAPSHOT"

crossScalaVersions := Seq("2.8.1", "2.9.0-1", "2.9.1")



resolvers += ("Sonatype Public" at "https://oss.sonatype.org/content/repositories/public/")

libraryDependencies <<= (scalaVersion, libraryDependencies) { (sv, deps) =>
	val versionMap = Map("2.8.1" -> "1.5.1", "2.9.0-1" -> "1.6.1", "2.9.1" -> "1.6.1")
	val testVersion = versionMap.getOrElse(sv, error("Unsupported Scala version " + sv))
	deps :+ ("org.scalatest" %% "scalatest" % testVersion % "test")
}

libraryDependencies ++= {
    Seq(
        "joda-time" % "joda-time" % "1.6.2" % "provided",
        "commons-httpclient" % "commons-httpclient" % "3.1",
        "org.slf4j" % "slf4j-api" % "1.6.1" % "provided",
        "ch.qos.logback" % "logback-classic" % "1.0.7" % "test"
    )
}

libraryDependencies += "org.jdom" % "jdom" % "1.1"

libraryDependencies += "org.mockito" % "mockito-core" % "1.9.0" % "test"

publishTo <<= (version) { version: String =>
  val nexus = "http://nexus.gudev.gnl:8081/nexus/content/repositories/"
  if (version.trim.endsWith("SNAPSHOT")) Some("Guardian Internal Snapshots" at nexus + "snapshots/")
  else                                   Some("Guardian Internal Releases"  at nexus + "releases/")
}

credentials += Credentials("Sonatype Nexus Repository Manager", "nexus.gudev.gnl", "deployment", "d3pl0y")

publishArtifact in (Compile, packageDoc) := false

publishArtifact in (Compile, packageSrc) := true
