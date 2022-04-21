package com.example

import cats.effect.{Sync, Resource}
import cats.syntax.all._
import java.io._

object PolimorficFileCopyExample {

  def transfer[F[_]: Sync](
      origin: InputStream,
      destination: OutputStream
  ): F[Long] = ???

  def inputStream[F[_]: Sync](f: File): Resource[F, FileInputStream] = ???

  def outputStream[F[_]: Sync](f: File): Resource[F, FileOutputStream] = ???

  def inputOutputStreams[F[_]: Sync](
      in: File,
      out: File
  ): Resource[F, (InputStream, OutputStream)] = ???

  def copy[F[_]: Sync](origin: File, destination: File): F[Long] = ???

  def transmit[F[_]: Sync](
      origin: InputStream,
      destination: OutputStream,
      buffer: Array[Byte],
      acc: Long
  ): F[Long] =
    for {
      amount <- Sync[F].blocking(origin.read(buffer, 0, buffer.size))
      count <-
        if (amount > -1)
          Sync[F].blocking(destination.write(buffer, 0, amount)) >> transmit(
            origin,
            destination,
            buffer,
            acc + amount
          )
        else Sync[F].pure(acc)
    } yield acc
}
