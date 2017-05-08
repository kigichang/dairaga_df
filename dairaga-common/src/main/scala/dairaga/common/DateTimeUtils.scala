package dairaga.common

import dairaga.env._
import java.util.{Date => JDate}
import java.text.{SimpleDateFormat}

/**
  * Created by kigi on 5/7/17.
  */
object DateTimeUtils {

  def format(time: JDate, fmt: String): String = new SimpleDateFormat(fmt).format(time)
  def format(time: Long, fmt: String): String = format(new JDate(time), fmt)

  def formatDate(date: JDate): String = format(date, DateFormat)
  def formatDate(date: Long): String = format(date, DateFormat)

  def formatTime(time: JDate): String = format(time, TimeFormat)
  def formatTime(time: Long): String = format(time, TimeFormat)

  def formatDateTime(date: JDate): String = format(date, DateTimeFormat)
  def formatDateTime(date: Long): String = format(date, DateTimeFormat)

  def parse(value: String, fmt: String): JDate = new SimpleDateFormat(fmt).parse(value)

  def parseDate(value: String) = parse(value, DateFormat)

  def parseDateTime(value: String) = parse(value, DateTimeFormat)

  def parseToLong(value: String, fmt: String): Long = parse(value, fmt).getTime

  def parseDateToLong(value: String): Long = parseToLong(value, DateFormat)

  def parseDateTimeToLong(value: String): Long = parseToLong(value, DateTimeFormat)
}
