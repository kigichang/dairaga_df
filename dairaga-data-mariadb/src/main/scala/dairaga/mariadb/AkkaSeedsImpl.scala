package dairaga.mariadb

import java.sql.{Connection, PreparedStatement, ResultSet, Statement}

import akka.actor.{Address, AddressFromURIString}
import com.typesafe.config.ConfigFactory
import dairaga.common.SQLUtils
import dairaga.data.AkkaSeeds

import scala.collection.immutable

/**
  * Akka Seeds implemented with MariaDB
  *
  * The table schema
  *
  * {{{
  *   CREATE TABLE `seeds` (
  *     `address` char(255) NOT NULL,
  *     `status` smallint(6) NOT NULL DEFAULT 0,
  *     `created` timestamp NOT NULL DEFAULT current_timestamp(),
  *     `updated` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  *     PRIMARY KEY (`address`)
  *   )
  * }}}
  *
  *
  * Created by kigi on 31/05/2017.
  */
class AkkaSeedsImpl extends AkkaSeeds {

  SQLUtils.driver("org.mariadb.jdbc.Driver")

  private val url = ConfigFactory.load().getString("dairaga.data.mariadb.url")

  override def seeds: immutable.Seq[Address] = {
    var conn: Connection = null
    var stmt: PreparedStatement = null
    var rs: ResultSet = null


    try {
      conn = SQLUtils.connect(url)
      stmt = SQLUtils.statement("select address from seeds where status = 1", conn)
      rs = SQLUtils.query(stmt)

      var ret = immutable.Seq.empty[Address]

      while(rs.next()) {
        val addr = AddressFromURIString(rs.getString("address"))
        ret = ret :+ addr
      }
      ret
    }
    finally {
      SQLUtils.close(rs)
      SQLUtils.close(stmt)
      SQLUtils.close(conn)
    }
  }

  override def write(address: Address): Unit = {
    val sql =
      s"""insert into seeds(address, status)
         |values('${address.toString}', 1)
         |on duplicate key update status = 1""".stripMargin


    SQLUtils.fastUpdate(url, sql)
  }

  override def remove(address: Address): Unit = {
    val sql = s"""delete from seeds where address = '${address.toString}'"""
    println(s"mariadb: $sql")
    SQLUtils.fastUpdate(url, sql)
  }
}
