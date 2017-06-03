package dairaga.master

import com.google.inject.Guice
import dairaga.akka._
import dairaga.cmd.{ConsoleCommand, ConsoleCommands, ExecCommand}
import dairaga.data.AkkaSeeds

/**
  * A master server to run master node
  *
  * Created by kigi on 31/05/2017.
  */
object MasterServer {


  val injector = Guice.createInjector(new MasterModule())

  val master = new MasterNode(injector.getInstance(classOf[AkkaSeeds]))

  def exec(cmd: String): Unit = {
    cmd match {
      case dairaga.cmd.Ping =>
        master.master ! XVPing

      case dairaga.cmd.Shutdown =>
        master.master ! XVShutdown

      case x if x.startsWith(dairaga.cmd.Close) =>
        master.master ! MasterActor.MasterClose(x.substring(dairaga.cmd.Close.length + 1))
      case _ =>
    }
  }

  def main(args: Array[String]): Unit = {

    master.run()

    ConsoleCommands("Master", exec).run

    master.shutdown()
  }
}
