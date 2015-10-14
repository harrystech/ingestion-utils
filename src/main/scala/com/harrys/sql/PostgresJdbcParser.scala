package com.harrys.sql

import java.net.URI

import org.postgresql.ds.PGSimpleDataSource

/**
 * Created by chris on 10/14/15.
 */
object PostgresJdbcParser {
  final def parseJdbcUrlForDataSource(jdbcUrl: String) : PGSimpleDataSource = {
    val output = new PGSimpleDataSource()
    output.setUrl(removeJdbcCredentials(jdbcUrl))

    extractJdbcCredentials(jdbcUrl).foreach({
      case (username, None) =>
        output.setUser(username)
      case (username, Some(pass)) =>
        output.setUser(username)
        output.setPassword(pass)
    })

    output
  }


  private final def extractJdbcCredentials(jdbcUrl: String) : Option[(String, Option[String])] = {
    val parsed = URI.create(removeJdbcPrefix(jdbcUrl))
    if (parsed.getUserInfo == null){
      None
    } else {
      val pieces = parsed.getUserInfo.split(":", 2)
      if (pieces.length == 1){
        Some((pieces(0), None))
      } else {
        Some((pieces(0), Some(pieces(1))))
      }
    }
  }

  /**
   * Strips the userinfo section of the URI leaving the rest of the connection info
   * @param jdbcUrl The (potentially) sensitive JDBC URL to sanitize
   * @return The sanitized URI without those credentials
   */
  final def removeJdbcCredentials(jdbcUrl: String) : String = {
    val uri = URI.create(removeJdbcPrefix(jdbcUrl))

    val scheme =
      if (uri.getScheme.equals("postgres")){
        "postgresql"
      } else {
        uri.getScheme
      }
    val user   = null
    val host   = uri.getHost()
    val port   = uri.getPort()
    val path   = uri.getPath()
    val query  = uri.getQuery()
    val frag   = uri.getFragment()

    return "jdbc:" + new URI(scheme, user, host, port, path, query, frag).toString()
  }

  /**
   * Removes a leading jdbc: prefix to the URI, making it managable for the URI parser
   * @param jdbcUrl The URL to remove the prefix from
   * @return The rest of the JDBC URL string with the prefix removed
   */
  private final def removeJdbcPrefix(jdbcUrl: String) : String = jdbcUrl.replaceFirst("^jdbc:", "")
}
