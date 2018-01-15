import ReleaseTransformations._

name := "exact-target-client"
organization := "com.gu"
scalaVersion := "2.12.4"
crossScalaVersions := Seq("2.11.4", scalaVersion.value)
resolvers += ("Sonatype Public" at "https://oss.sonatype.org/content/repositories/public/")
libraryDependencies ++= {
  Seq(
    "joda-time"               % "joda-time"               % "2.3",
    "org.joda"                % "joda-convert"            % "1.6",
    "commons-httpclient"      % "commons-httpclient"      % "3.1",
    "org.jdom"                % "jdom"                    % "1.1",
    "org.slf4j"               % "slf4j-api"               % "1.6.1"   % "provided",
    "org.mockito"             % "mockito-core"            % "1.9.5"   % "test",
    "ch.qos.logback"          % "logback-classic"         % "1.0.7"   % "test",
    "org.scalatest"           %% "scalatest"              % "3.0.4"   % "test",
    "org.scala-lang.modules"  %% "scala-xml"              % "1.0.6"
  )
}
publishTo := Some(
  if (isSnapshot.value) Opts.resolver.sonatypeSnapshots
  else Opts.resolver.sonatypeReleases
)
publishArtifact in (Compile, packageDoc) := false
publishArtifact in (Compile, packageSrc) := true
releaseCrossBuild := true
releasePublishArtifactsAction := PgpKeys.publishSigned.value
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  publishArtifacts,
  setNextVersion,
  commitNextVersion,
  releaseStepCommand("sonatypeReleaseAll"),
  pushChanges
)
