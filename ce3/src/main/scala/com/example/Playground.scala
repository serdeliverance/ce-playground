package com.example

import cats.effect.IOApp
import cats.effect.IO
import scala.concurrent.duration._

object Playground extends IOApp.Simple {

  val run =
    (IO.println("Hola") >> IO.delay(8)).map(v => println(v)).as(())

  // cancelation example
  lazy val loop: IO[Unit] = IO.println("Hi") >> loop

  loop.timeout(5.seconds)

  // another example
  val interruptible = IO.interruptible(Thread.sleep(1000))
}
