package com.harrys.test

import java.io.File
import java.nio.file.Files

import com.harrys.hyppo.source.api.RawDataIntegration
import com.harrys.hyppo.source.api.data.{AvroRecordAppender, RawDataCollector, AvroRecordType}
import com.harrys.hyppo.source.api.model.DataIngestionTask
import com.harrys.hyppo.source.api.task.{PersistProcessedData, ProcessRawData, FetchRawData, CreateIngestionTasks}
import org.apache.avro.specific.SpecificRecord
import org.scalatest.{Matchers, WordSpec}

import scala.collection.JavaConversions

/**
 * Created by chris on 12/26/15.
 */
abstract class HyppoIntegrationTest[T <: SpecificRecord] extends WordSpec with Matchers {
  val integration : RawDataIntegration[T]
  val avroType : AvroRecordType[T]

  val testJob = TestConfig.createNoParameterJob()
  val rawData = Files.createTempDirectory("avro").toFile

  var tasks : Seq[DataIngestionTask] = Seq()
  var rawFiles : Seq[File]           = Seq()
  var avroFiles : Seq[File]          = Seq()

  def setTasks(tasks: Seq[DataIngestionTask]) : Unit = {
    this.tasks = tasks.map(t => new DataIngestionTask(testJob, t.getTaskNumber, t.getTaskArguments))
  }

  def addRawData(file: File): Unit = {
    rawFiles :+= file
  }

  def addAvroData(file: File): Unit = {
    avroFiles :+= file
  }

  def generateTasksList() : java.util.List[DataIngestionTask] = {
    val createTasks = new CreateIngestionTasks(testJob)
    integration.newIngestionTaskCreator().createIngestionTasks(createTasks)
    val created = createTasks.getTaskBuilder.build()
    setTasks(JavaConversions.asScalaBuffer(created))
    created
  }

  def generateRawDataFetches : Seq[FetchRawData] = {
    val fetches = tasks.map(t => new FetchRawData(t, new RawDataCollector(rawData)))
    fetches.foreach { fetch =>
      integration.newRawDataFetcher().fetchRawData(fetch)
      val filesSeq = scala.collection.JavaConversions.asScalaBuffer(fetch.getDataFiles).toSeq
      filesSeq.foreach { file =>
        addRawData(file)
      }
    }
    fetches
  }

  def processAndAppendRawData(task: DataIngestionTask, file: File) : AvroRecordAppender[T] = {
    val outFile = Files.createTempFile("processed", "avro").toFile
    val appender = avroType.createAvroRecordAppender(outFile)
    val process = new ProcessRawData[T](task, file, appender)
    integration.newRawDataProcessor().processRawData(process)
    appender.close()
    addAvroData(appender.getOutputFile)
    appender
  }

  def persistAvroRecordsToDatabase(task: DataIngestionTask, file: File) : Unit = {
    val persist = new PersistProcessedData[T](task, avroType, file)
    integration.newProcessedDataPersister().persistProcessedData(persist)
  }
}
