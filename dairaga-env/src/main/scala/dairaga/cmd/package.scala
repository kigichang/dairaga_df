package dairaga

import scala.io.StdIn
import scala.util.control.NonFatal

/**
  * Created by kigi on 31/05/2017.
  */
package object cmd {
  val help = ":help"
  val Quit = ":quit"
  val Ping = ":ping"
  val Shutdown = ":shutdown"
  val Close = ":close"  // :close ip:port or :close ip or :close port

  type ExecCommand = PartialFunction[String, Unit]

  val defaultCommand: ExecCommand = {
    case _ => println("sss " + _)
  }

  object ConsoleCommands {
    def apply(tag: String, exec: String => Unit): ConsoleCommand = {
      ConsoleCommand(tag, exec)
    }
  }

  case class ConsoleCommand(tag: String, exec: String => Unit) {

    def run = {
      var cmd = StdIn.readLine(s"$tag >>")
      while(cmd != Quit) {
        if (cmd != "") {
          println(s"exec `$cmd`...")
          try { exec(cmd) }
          catch { case NonFatal(ex) => println(s"error: ${ex.toString}") }
        }
        cmd = StdIn.readLine(s"$tag >>")
      }
    }
  }
}
