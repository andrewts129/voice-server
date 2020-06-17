package io.andrewsmith.voiceserver

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

import scala.concurrent.ExecutionContext.global
import scala.sys.process._
import cats.effect._
import org.http4s.{HttpRoutes, MediaType}
import org.http4s.dsl.io._
import org.http4s.headers.`Content-Type`
import org.http4s.implicits._
import org.http4s.server.blaze._

object Main extends IOApp {
  private def getWav(text: String): Array[Byte] = {
    val inputStream = new ByteArrayInputStream(text.getBytes)
    val outputStream = new ByteArrayOutputStream()

    val exitCode = "text2wave" #< inputStream #> outputStream !< ProcessLogger(_ => ())
    exitCode match {
      case 0 => outputStream.toByteArray
      case _ => throw new RuntimeException(s"Nonzero exit value ($exitCode) for input: $text")
    }
  }

  private val service = HttpRoutes.of[IO] {
    case GET -> Root / text =>
      Ok(getWav(text), `Content-Type`(MediaType.audio.wav))
  }.orNotFound

  override def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO](global)
      .bindHttp(3000, "0.0.0.0")
      .withHttpApp(service)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}
