package com.harrys.http

import java.io._

import com.harrys.file.TransientFileFactory
import com.harrys.util.Timer
import com.typesafe.scalalogging.Logger
import org.apache.commons.io.IOUtils
import org.apache.http.client.methods.{CloseableHttpResponse, HttpUriRequest}
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.util.EntityUtils
import org.json4s.jackson.JsonMethods
import org.json4s.{Formats, JValue}
import org.slf4j.LoggerFactory

/**
 * Created by chris on 10/14/15.
 */
abstract class AbstractHttpClient {
  private val log = Logger(LoggerFactory.getLogger(this.getClass))

  protected def createHttpClient() : CloseableHttpClient

  final def performRequestForJson(request: HttpUriRequest)(implicit formats: Formats) : (HttpResponseSummary, JValue) = {
    this.performRequest(request) { response =>
      val summary = new HttpResponseSummary(response)
      (summary, JsonMethods.parse(response.getEntity.getContent))
    }
  }

  final def performRequestToBuffer(request: HttpUriRequest) : (HttpResponseSummary, Array[Byte]) = {
    this.performRequest(request) { response =>
      response.getEntity.getContentEncoding
      val summary = new HttpResponseSummary(response)
      (summary, EntityUtils.toByteArray(response.getEntity))
    }
  }

  final def performRequestToLocalFile(request: HttpUriRequest) : (HttpResponseSummary, File) = {
    this.performRequest(request) { response =>
      response.getEntity.getContentType
      val summary = new HttpResponseSummary(response)
      (summary, copyResponseToFile(response))
    }
  }

  private final def copyResponseToFile(response: CloseableHttpResponse) : File = {
    val tempFile   = TransientFileFactory.create("httpraw", "response")
    val fileStream = new FileOutputStream(tempFile)
    try {
      response.getEntity.writeTo(fileStream)
      return tempFile
    } finally {
      IOUtils.closeQuietly(fileStream)
    }
  }


  final def performRequest[T](request: HttpUriRequest)(handler: (CloseableHttpResponse) => T) : T = {
    val client = this.createHttpClient()

    try {
      val (duration, httpResult) = Timer.timeWithAttempt { client.execute(request) }

      httpResult match {
        case util.Failure(cause) =>
          log.error(s"[${ request.getMethod }] ${ request.getURI.getPath }(${ duration.toMillis.toString })", cause)
          throw cause
        case util.Success(response) =>
          log.debug(s"${} [${ request.getMethod }] ${ response.getStatusLine.getStatusCode } ${ request.getURI.getPath } (${ duration.toMillis.toString })")
          try {
            handler(response)
          } finally {
            IOUtils.closeQuietly(response)
          }
      }
    } finally {
      IOUtils.closeQuietly(client)
    }
  }
}
