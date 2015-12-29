package com.harrys.test

import java.io.{FileInputStream, File}
import java.util.{Date, UUID, Properties}
import com.typesafe.config.{ConfigFactory, Config}
import com.harrys.hyppo.source.api.model.{DataIngestionJob, IngestionSource}

/**
 * Created by chris on 12/26/15.
 */
object TestConfig {
  final val EnvFileProperty = "test.env-file"

  def basicIngestionSource(): IngestionSource = {
    new IngestionSource("Hyppo Ingestion Test", testConfig())
  }

  def createNoParameterJob(): DataIngestionJob = {
    val source = basicIngestionSource()
    val params = ConfigFactory.empty()
    new DataIngestionJob(source, UUID.randomUUID(), params, new Date())
  }

  def testConfig(): Config = {
    val testProps = ConfigFactory.parseProperties(testProperties())
    ConfigFactory.parseResources("testing.conf").resolveWith(testProps)
  }

  def testProperties(): Properties = {
    val props = new Properties()
    val sys   = System.getProperties
    if (sys.containsKey(EnvFileProperty)){
      val env = new File(sys.getProperty(EnvFileProperty))
      if (!env.exists){
        throw new IllegalArgumentException(s"-Dtest.env-file value of ${ env.getAbsolutePath } is invalid. File does not exist.")
      } else {
        val stream = new FileInputStream(env)
        try {
          props.load(stream)
        } finally {
          stream.close()
        }
      }
    }
    props
  }
}
