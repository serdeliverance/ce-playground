package com.example

import cats.effect.IO
import cats.effect.IOApp

object HelloWorld extends IOApp.Simple {
  val run = IO.println("Hello!!")
}
