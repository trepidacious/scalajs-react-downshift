import sbtcrossproject.CrossPlugin.autoImport.crossProject

name := "scalajs-react-downshift root"

version in ThisBuild := "0.0.1-SNAPSHOT"

organization in ThisBuild := "org.rebeam"

scalaVersion in ThisBuild := "2.12.6"

// crossScalaVersions in ThisBuild := Seq("2.11.12", "2.12.6")

scalacOptions in ThisBuild ++= Seq(
  "-feature",
  "-deprecation",
  "-encoding", "UTF-8",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Xcheckinit",
  "-Xlint:-unused",
  "-Ywarn-unused:imports",
  "-Ypartial-unification",
  "-language:existentials",
  "-language:higherKinds",
  "-Yno-adapted-args",
  // "-Ywarn-dead-code",  //TODO restore for JVM and shared only
  // "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture"
  //"-Yno-predef" ?
)

lazy val scalajsReactVersion        = "1.2.3"

lazy val root = project.in(file(".")).
  aggregate(scalajsReactDownshiftJS, scalajsReactDownshiftJVM).
  settings(
    publish := {},
    publishLocal := {}
  )

lazy val scalajsReactDownshift = crossProject(JSPlatform, JVMPlatform).in(file(".")).
  //Settings for all projects
  settings(
    name := "scalajs-react-downshift",
    libraryDependencies ++= Seq(
    ),

  ).jvmSettings(
      // mainClass := Some("org.rebeam.Generate"),

  ).jsSettings(
    //Scalajs dependencies that are used on the client only
    resolvers += Resolver.jcenterRepo,

    libraryDependencies ++= Seq(
      "com.github.japgolly.scalajs-react" %%% "core" % scalajsReactVersion,
      "com.github.japgolly.scalajs-react" %%% "extra" % scalajsReactVersion
    ),
    
    //Produce a module, so we can use @JSImport.
    // scalaJSModuleKind := ModuleKind.CommonJSModule//,
    scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) }
  )

lazy val scalajsReactDownshiftJVM = scalajsReactDownshift.jvm
lazy val scalajsReactDownshiftJS = scalajsReactDownshift.js

