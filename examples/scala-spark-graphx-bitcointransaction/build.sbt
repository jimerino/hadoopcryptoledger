
import sbt._
import Keys._
import scala._


lazy val root = (project in file("."))
.settings(
    name := "example-hcl-spark-scala-graphx-bitcointransaction",
    version := "0.1"
)
 .configs( IntegrationTest )
  .settings( Defaults.itSettings : _*)
.enablePlugins(JacocoItPlugin)
 

scalacOptions += "-target:jvm-1.7"

scalaVersion := "2.11.8"

resolvers += Resolver.mavenLocal

fork  := true



assemblyJarName in assembly := "example-hcl-spark-scala-graphx-bitcointransaction.jar"

libraryDependencies += "com.github.zuinnote" % "hadoopcryptoledger-fileformat" % "1.2.0" % "compile"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.3.0" % "provided"

libraryDependencies += "org.apache.spark" %% "spark-graphx" % "2.3.0" % "provided"

libraryDependencies += "org.apache.hadoop" % "hadoop-client" % "2.7.0" % "provided"

libraryDependencies += "javax.servlet" % "javax.servlet-api" % "3.0.1" % "it"


libraryDependencies += "org.apache.hadoop" % "hadoop-common" % "2.7.0" % "it" classifier "" classifier "tests"

libraryDependencies += "org.apache.hadoop" % "hadoop-hdfs" % "2.7.0" % "it" classifier "" classifier "tests"

libraryDependencies += "org.apache.hadoop" % "hadoop-minicluster" % "2.7.0" % "it"


libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test,it"
