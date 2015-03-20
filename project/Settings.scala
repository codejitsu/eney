import de.johoop.jacoco4sbt.JacocoPlugin.jacoco
import de.johoop.jacoco4sbt.Thresholds
import org.scalastyle.sbt.ScalastylePlugin
import sbt._
import sbt.Keys._
import scala.language.postfixOps

object Settings extends Build {
  lazy val buildSettings = Seq(
    name                  := "milandb",
    normalizedName        := "milandb",
    organization          := "milandb",
    organizationHomepage  := Some(url("http://www.milandb.com")),
    scalaVersion          := Versions.ScalaVer,
    homepage              := Some(url("http://www.milandb.com"))
  )

  override lazy val settings = super.settings ++ buildSettings

  val parentSettings = buildSettings ++ Seq(
    publishArtifact := false,
    publish         := {}
  )

  val scalacSettings = Seq("-encoding", "UTF-8", s"-target:jvm-${Versions.JDKVer}", "-feature", "-language:_",
    "-deprecation", "-unchecked", "-Xfatal-warnings", "-Xlint", "-language:higherKinds")

  val javacSettings = Seq("-encoding", "UTF-8", "-source", Versions.JDKVer,
    "-target", Versions.JDKVer, "-Xlint:deprecation", "-Xlint:unchecked")

  lazy val defaultSettings = testSettings ++ Seq(
    autoCompilerPlugins := true,
   // libraryDependencies <+= scalaVersion { v => compilerPlugin("org.brianmckenna" %% "wartremover" % Versions.WartremoverVer) },
    scalacOptions       ++= scalacSettings,
   // scalacOptions       += "-P:wartremover:traverser:org.brianmckenna.wartremover.warts.Unsafe",
    javacOptions        in Compile    ++= javacSettings,
    ivyLoggingLevel     in ThisBuild  := UpdateLogging.Quiet,
    parallelExecution   in ThisBuild  := false,
    parallelExecution   in Global     := false
  ) ++ com.twitter.scrooge.ScroogeSBT.newSettings ++ net.virtualvoid.sbt.graph.Plugin.graphSettings

  val tests = inConfig(Test)(Defaults.testTasks) ++ inConfig(IntegrationTest)(Defaults.itSettings)

  val testOptionSettings = Seq(
    Tests.Argument(TestFrameworks.ScalaTest, "-oDF"),
    Tests.Argument(TestFrameworks.JUnit, "-oDF", "-v", "-a")
  )

  lazy val testSettings = tests ++ jacoco.settings ++ ScalastylePlugin.Settings ++ Seq(
    parallelExecution in Test             := false,
    parallelExecution in IntegrationTest  := false,
    testOptions       in Test             ++= testOptionSettings,
    testOptions       in IntegrationTest  ++= testOptionSettings,
    fork              in Test             := true,
    fork              in IntegrationTest  := true,
    (compile in IntegrationTest)        <<= (compile in Test, compile in IntegrationTest) map { (_, c) => c },
    managedClasspath in IntegrationTest <<= Classpaths.concat(managedClasspath in IntegrationTest, exportedProducts in Test),
    jacoco.thresholds in jacoco.Config := Thresholds(instruction = 70, method = 70, branch = 70, complexity = 70, line = 70, clazz = 70)
  )
}