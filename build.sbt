import Dependencies._

ThisBuild / organization := "com.example"
ThisBuild / scalaVersion := "2.13.5"

lazy val ce3 = (project in file("ce3")).settings(
  name := "ce3-learning",
  libraryDependencies ++= Seq(
    catsEffect,
    catsEffectKernel,
    catsEffectStd,
    munitCatsEffect % Test,
    compilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
  )
)
