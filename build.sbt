name := "HW1"

version := "0.1"

scalaVersion := "2.12.13"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x                             => MergeStrategy.first
}

libraryDependencies ++= Seq(
  "org.apache.hadoop" % "hadoop-auth" % "3.3.0",
  "org.apache.hadoop" % "hadoop-client" % "3.3.0",
  "org.scalatest" %% "scalatest" % "3.2.5" % Test,
  "org.mockito" %% "mockito-scala" % "1.9.0" % Test,
  "org.scalatestplus" %% "mockito-3-4" % "3.2.5.0" % Test
)
