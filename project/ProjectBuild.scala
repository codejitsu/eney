import sbt._
import sbt.Keys._

object ProjectBuild extends Build {
  import Settings._

  lazy val root = Project(
    id = "root",
    base = file("."),
    settings = parentSettings,
    aggregate = Seq(milanCore, milanStore, milanCrdt, milanCli)
  )

  lazy val milanCrdt = Project(
    id = "milan-crdt",
    base = file("./milan-crdt"),
    settings = defaultSettings ++ Seq(libraryDependencies ++= Dependencies.milanCrdt)
  )

  lazy val milanStore = Project(
    id = "milan-store",
    base = file("./milan-store"),
    settings = defaultSettings ++ Seq(libraryDependencies ++= Dependencies.milanStore)
  ).dependsOn(milanCrdt)

  lazy val milanCore = Project(
    id = "milan-core",
    base = file("./milan-core"),
    settings = defaultSettings ++ Seq(libraryDependencies ++= Dependencies.milanCore)
  ).dependsOn(milanStore)

  lazy val milanCli = Project(
    id = "milan-cli",
    base = file("./milan-cli"),
    settings = defaultSettings ++ Seq(libraryDependencies ++= Dependencies.milanCli)
  )
}

object Dependencies {
  import Versions._

  object Compile {
    val config        = "com.typesafe"             % "config"           % TypesafeConfigVer
  }

  object Test {
    val scalatest     = "org.scalatest"            % "scalatest_2.10"       % ScalaTestVer      % "test"
    val scalacheck    = "org.scalacheck"          %% "scalacheck"           % ScalaCheckVer     % "test"
    val junit         = "junit"                    % "junit"                % JunitVer          % "test"

    val abideExtra    = "com.typesafe"             % "abide-extra_2.11"     % AbideExtraVer     % "abide,test"
  }

  object Twitter {
    val finagle       = "com.twitter" % "finagle-core_2.10"     % FinagleVer
    val finagleStats  = "com.twitter" % "finagle-stats_2.10"    % FinagleVer
    val finagleThrift = "com.twitter" % "finagle-thrift_2.10"   % FinagleVer
    val scrooge       = "com.twitter" % "scrooge-core_2.10"     % ScroogeVer
    val twitterServer = "com.twitter" % "twitter-server_2.10"   % TwitterServerVer
  }

  import Compile._

  val test = Seq(Test.scalatest, Test.scalacheck, Test.junit)

  val twitter = Seq(Twitter.finagle, Twitter.finagleStats, Twitter.finagleThrift,
    Twitter.scrooge, Twitter.twitterServer)

  /** Module deps */

  val milanCore = Seq(config) ++ test ++ twitter
  val milanStore = Seq(config) ++ test
  val milanCrdt = test
  val milanCli = test
}
