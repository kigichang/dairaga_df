package dairaga.common

import java.sql._
import scala.util.control.NonFatal

/**
  * Created by kigi on 5/7/17.
  */
object SQL {

  def close(resouce: AutoCloseable) = {
    try {
      if (resouce != null)
        resouce.close()
    }
    catch {
      case NonFatal(ex) =>
    }
  }

  def statement(cmd: String, connection: Connection): PreparedStatement = connection.prepareStatement(cmd)

}
