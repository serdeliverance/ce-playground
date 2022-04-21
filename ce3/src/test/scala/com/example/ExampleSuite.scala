package com.example

import munit.CatsEffectSuite
import cats.effect.IO

class ExampleSuite extends CatsEffectSuite {
  test("make sure IO computes the right result") {
    IO.pure(1).map(_ + 2).flatMap { result =>
      IO(assertEquals(result, 3))
    }
  }
}
