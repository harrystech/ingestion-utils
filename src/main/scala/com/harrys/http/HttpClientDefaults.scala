package com.harrys.http

import org.apache.http.client.config.RequestConfig
import org.apache.http.impl.client.{HttpClientBuilder, HttpClients}

import scala.concurrent.duration._

/**
 * Created by chris on 10/14/15.
 */
object HttpClientDefaults {
  val ConnectTimeout = FiniteDuration(10, SECONDS)
  val SocketTimeout  = FiniteDuration(30, SECONDS)

  def defaultRequestConfig : RequestConfig.Builder = RequestConfig.copy(RequestConfig.DEFAULT)
    .setConnectTimeout(ConnectTimeout.toMillis.toInt)
    .setSocketTimeout(SocketTimeout.toMillis.toInt)


  def defaultHttpClientBuilder : HttpClientBuilder = HttpClients.custom()
    .setDefaultRequestConfig(defaultRequestConfig.build())
}
