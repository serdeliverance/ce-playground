package com.example

import java.io.File
import cats.effect.{IO, Resource}
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import cats.effect.IOApp
import cats.effect.ExitCode

object FileCopyExample extends IOApp {

  def inputStream(f: File): Resource[IO, FileInputStream] =
    Resource.make {
      IO.blocking(new FileInputStream(f))
    } { inStream =>
      IO.blocking(inStream.close()).handleErrorWith(_ => IO.unit)
    }

  def outputStream(f: File): Resource[IO, FileOutputStream] =
    Resource.make {
      IO.blocking(new FileOutputStream(f))
    } { outStream =>
      IO.blocking(outStream.close()).handleErrorWith(_ => IO.unit)
    }

  def inputOutputStreams(
      in: File,
      out: File
  ): Resource[IO, (InputStream, OutputStream)] =
    for {
      inStream <- inputStream(in)
      outStream <- outputStream(out)
    } yield (inStream, outStream)

  /** This method could be used when the resources implements [[Autocloseable]].
    * However, it let us less control to the release cycle.
    */
  def inputStream_v2(f: File): Resource[IO, FileInputStream] =
    Resource.fromAutoCloseable(IO(new FileInputStream(f)))

  def transmit(
      origin: InputStream,
      destination: OutputStream,
      buffer: Array[Byte],
      acc: Long
  ): IO[Long] =
    for {
      amount <- IO.blocking(origin.read(buffer, 0, buffer.size))
      count <-
        if (amount > -1)
          IO.blocking(destination.write(buffer, 0, amount)) >> transmit(
            origin,
            destination,
            buffer,
            acc + amount
          )
        else IO.pure(acc)
    } yield count

  def transfer(origin: InputStream, destination: OutputStream): IO[Long] =
    transmit(origin, destination, new Array[Byte](1024 * 10), 0L)

  def copy(origin: File, destination: File): IO[Long] =
    inputOutputStreams(origin, destination).use { case (in, out) =>
      transfer(in, out)
    }

  /** Main program
    */
  override def run(args: List[String]): IO[ExitCode] =
    for {
      _ <-
        if (args.length < 2)
          IO.raiseError(
            new IllegalArgumentException("Need origin and destination files")
          )
        else IO.unit
      orig = new File(args(0))
      dest = new File(args(1))
      count <- copy(orig, dest)
      _ <- IO.println(
        s"$count bytes copied from ${orig.getPath} to ${dest.getPath}"
      )
    } yield ExitCode.Success
}
