// Your profile name of the sonatype account. The default is the same with the organization value
sonatypeProfileName := "com.gu"

// To sync with Maven central, you need to supply the following information:
publishMavenStyle := true

// License of your choice
licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))
homepage := Some(url("https://github.com/guardian/request-authentication-filter"))
scmInfo := Some(
  ScmInfo(
    url("https://github.com/guardian/exact-target-client"),
    "scm:git@github.com:guardian/exact-target-client.git"
  )
)
developers := List(
  Developer(id="mario-galic", name="Mario Galic", email="mario.galic@guardian.co.uk", url=url("https://github.com/mario-galic"))
)