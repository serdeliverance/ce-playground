package com.example

import cats.effect.std.Console
import cats.Monad
import cats.syntax.all._

object AnotherExample {

  def example[F[_]: Monad: Console](str: String): F[String] = {
    val printer: F[Unit] = Console[F].println(str)
    (printer >> printer).as(str)
  }
}
