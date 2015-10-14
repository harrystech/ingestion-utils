package com.harrys.http

import org.apache.http.{Header, HttpResponse, StatusLine}

/**
 * Created by chris on 10/14/15.
 */
class HttpResponseSummary(response: HttpResponse) {

  def statusLine: StatusLine  = response.getStatusLine

  def statusCode: Int         = statusLine.getStatusCode

  def reasonPhrase: String    = statusLine.getReasonPhrase

  def contentType: Header     = response.getEntity.getContentType

  def contentLength: Long     = response.getEntity.getContentLength

  def contentEncoding: Header = response.getEntity.getContentEncoding

  def allHeaders: Seq[Header] = response.getAllHeaders
}
