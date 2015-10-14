package com.harrys.util

import io.ingestion.source.api.task._
import org.apache.avro.specific.SpecificRecord

import scala.collection.JavaConversions

/**
 * Created by chris on 10/14/15.
 */
object ApiConversion {
  import scala.language.implicitConversions

  final class WrappedTaskCreator(creator: (CreateTasksForJob) => Any) extends TaskCreator {
    override def createTasks(operation: CreateTasksForJob): Unit = creator(operation)
  }

  final class WrappedRawDataFetcher(fetcher: (FetchRawData) => Any) extends RawDataFetcher {
    override def fetchRawData(operation: FetchRawData): Unit = fetcher(operation)
  }

  final class WrappedProcessedDataFetcher[T <: SpecificRecord](fetcher: (FetchProcessedData[T]) => Any) extends ProcessedDataFetcher[T] {
    override def fetchProcessedData(operation: FetchProcessedData[T]): Unit = fetcher(operation)
  }

  final class WrappedRawDataProcessor[T <: SpecificRecord](processor: (ProcessRawData[T]) => Any) extends RawDataProcessor[T] {
    override def processRawData(operation: ProcessRawData[T]): Unit = processor(operation)
  }

  final class WrappedProcessedDataPersister[T <: SpecificRecord](persister: (PersistProcessedData[T]) => Any) extends ProcessedDataPersister[T] {
    override def persistProcessedData(operation: PersistProcessedData[T]): Unit = persister(operation)
  }

  implicit def func2TaskCreator(creator: (CreateTasksForJob) => Any) : TaskCreator = {
    new WrappedTaskCreator(creator)
  }

  implicit def func2RawDataFetcher(fetcher: (FetchRawData) => Any): RawDataFetcher = {
    new WrappedRawDataFetcher(fetcher)
  }

  implicit def func2RawDataProcessor[T <: SpecificRecord](processor: (ProcessRawData[T]) => Any) : RawDataProcessor[T] = {
    new WrappedRawDataProcessor[T](processor)
  }

  implicit def func2ProcessedDataFetcher[T <: SpecificRecord](fetcher: (FetchProcessedData[T]) => Any) : ProcessedDataFetcher[T] = {
    new WrappedProcessedDataFetcher[T](fetcher)
  }

  implicit def func2ProcessedDataPersister[T <: SpecificRecord](persister: (PersistProcessedData[T]) => Any) : ProcessedDataPersister[T] = {
    new WrappedProcessedDataPersister[T](persister)
  }


  //  Operation Objects
  final implicit class WrappedCreateTasksForJob(creator: CreateTasksForJob) {
    final def createTaskWithArgs(args: Map[String, AnyRef]) : CreateTasksForJob = {
      val converted = JavaConversions.mapAsJavaMap(args)
      creator.createTaskWithArgs(converted)
    }
  }

  final implicit class WrappedProcessRawData[T <: SpecificRecord](process: ProcessRawData[T]) {
    final def append(records: Iterable[T]) : Unit = {
      process.append(JavaConversions.asJavaIterable(records))
    }

    final def append(records: Iterator[T]) : Unit = {
      process.append(JavaConversions.asJavaIterator(records))
    }
  }

  final implicit class WrappedFetchProcessedData[T <: SpecificRecord](fetch: FetchProcessedData[T]) {
    final def append(records: Iterable[T]) : Unit = {
      fetch.append(JavaConversions.asJavaIterable(records))
    }

    final def append(records: Iterator[T]) : Unit = {
      fetch.append(JavaConversions.asJavaIterator(records))
    }
  }
}
