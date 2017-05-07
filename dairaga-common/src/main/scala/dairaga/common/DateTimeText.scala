package dairaga.common

/**
  * Created by kigi on 5/7/17.
  */
object DateTimeText {

  import java.util.{Date => JDate}
  import java.text.SimpleDateFormat

  def format(time: JDate, fmt: String): String = new SimpleDateFormat(fmt).format(time)
  def format(time: Long, fmt: String): String = format(new JDate(time), fmt)

  def parse(value: String, fmt: String): JDate = new SimpleDateFormat(fmt).parse(value)
  def parseToLong(value: String, fmt: String): Long = parse(value, fmt).getTime
}
