package io.andrewsmith.voiceserver

import java.io.ByteArrayOutputStream

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
    val out = new ByteArrayOutputStream()
    val exitCode = s"echo $text" #| "text2wave" #> out !< ProcessLogger(_ => ())
    exitCode match {
      case 0 => out.toByteArray
      case _ => throw new RuntimeException(s"error")
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
