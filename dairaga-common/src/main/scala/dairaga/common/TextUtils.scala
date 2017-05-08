package dairaga.common

import dairaga.env._
import java.util.UUID
import scala.util.Random

import org.apache.commons.lang3.{ StringUtils => SU, CharUtils}

/**
  * Created by kigi on 08/05/2017.
  */
object TextUtils {

  def randomAlphaNumeric(length: Int, sep: String = ""): String = Random.alphanumeric take length mkString(sep)

  def uuid = java.util.UUID.randomUUID()

  def uuidFromString(name: String): UUID = java.util.UUID.fromString(name)

  def urlEncode(url: String, enc: String = Charset) = java.net.URLEncoder.encode(url, enc)

  def urlDecode(url: String, enc: String = Charset) = java.net.URLDecoder.decode(url, enc)

  def replaceHtml(input: String, replacement: String = SU.EMPTY): String = SU.replaceAll(input, "<[^>]*>", replacement).trim

  def replaceTab(input: String, newChar: Char = ' '): String = SU.replaceChars(input, '\t', newChar)

  def replaceCRLF(input: String, replacement: String = SU.EMPTY): String = SU.replace(SU.replaceChars(input, SU.CR, SU.EMPTY), SU.LF, replacement)

  def replaceNBSP(input: String, replacement: String = SU.SPACE): String = SU.replaceIgnoreCase(input, "&nbsp;", replacement)
}
