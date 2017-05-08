package dairaga.common

import java.sql._
import scala.util.control.NonFatal

/**
  * Created by kigi on 5/7/17.
  */
object SQLUtils extends IOUtils {

  def driver(clazz: String): Any = Class.forName(clazz).newInstance()

  def connect(url: String) = DriverManager.getConnection(url)

  def connect(url: String, account: String, password: String) = DriverManager.getConnection(url, account, password)

  def statement(cmd: String, connection: Connection): PreparedStatement = connection.prepareStatement(cmd)

  def query(statement: PreparedStatement): ResultSet = statement.executeQuery()

  def update(statement: PreparedStatement): Int = statement.executeUpdate()

  def fastQuery(url: String, sql: String): Option[String] = {
    var conn: Connection = null
    var stmt: PreparedStatement = null
    var rs: ResultSet = null

    try {
      conn = connect(url)
      stmt = statement(sql, conn)
      rs = query(stmt)

      if (rs.next()) Option(rs.getString(1))
      else None
    }

    finally {
      close(rs)
      close(stmt)
      close(conn)
    }
  }

  def fastUpdate(url: String, sql: String): Int = {
    var conn: Connection = null
    var stmt: PreparedStatement = null

    try {
      conn = connect(url)
      stmt = statement(sql, conn)
      update(stmt)
    }
    finally {
      close(stmt)
      close(conn)
    }
  }
}
