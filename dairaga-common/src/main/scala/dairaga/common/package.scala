package dairaga

import scala.util.control.NonFatal

/**
  * Created by kigi on 08/05/2017.
  */
package object common {
  trait IOUtils {
    def close(resource: AutoCloseable): Unit = {
      try {
        if (resource != null)
          resource.close()
      }
      catch {
        case NonFatal(ex) =>
      }
    }
  }
}
