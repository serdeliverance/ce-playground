import sbt._

object Dependencies {

  object V {
    val catsEffect = "3.3.11"
    val munitCatsEffect = "1.0.7"
  }

  // "core" module - IO, IOApp, schedulers
  // This pulls in the kernel and std modules automatically.
  val catsEffect = "org.typelevel" %% "cats-effect" % V.catsEffect

  // concurrency abstractions and primitives (Concurrent, Sync, Async etc.)
  val catsEffectKernel =
    "org.typelevel" %% "cats-effect-kernel" % V.catsEffect

  // standard "effect" library (Queues, Console, Random etc.)
  val catsEffectStd = "org.typelevel" %% "cats-effect-std" % V.catsEffect

  val munitCatsEffect =
    "org.typelevel" %% "munit-cats-effect-3" % V.munitCatsEffect
}
