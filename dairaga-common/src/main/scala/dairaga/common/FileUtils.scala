package dairaga.common

import dairaga.env._
import java.io.{BufferedWriter, FileOutputStream, OutputStreamWriter}
import scala.io.{BufferedSource, Source}
import scala.util.Try

/**
  * Created by kigi on 08/05/2017.
  */
object FileUtils extends IOUtils {

  def read(file: String, charset: String = "UTF-8"): Array[String] = {
    var src: BufferedSource = null

    try {
      src = Source.fromFile(file, charset)
      src.getLines().toArray
    }
    finally {
      close(src)
    }
  }

  def write(file: String, content: String, append: Boolean, charset: String): Boolean = Try {
    val bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), charset))
    bw.write(content)
    close(bw)
  }.isSuccess

  def write(file: String, content: String): Boolean = write(file, content, false, Charset)

}
