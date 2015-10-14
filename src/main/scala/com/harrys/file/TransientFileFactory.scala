package com.harrys.file

import java.io.File
import java.nio.file.attribute.FileAttribute
import java.nio.file.{Files, Path}
import java.util.concurrent._

import com.typesafe.scalalogging.Logger
import org.apache.commons.io.FileUtils
import org.slf4j.LoggerFactory

import scala.collection.mutable
import scala.ref._

/**
 * Created by chris on 10/14/15.
 */
class TransientFileFactory(directory: Path) {
  private val log = Logger(LoggerFactory.getLogger(this.getClass))

  private val pendingRefFiles = new mutable.HashSet[Reference[File]]()
  private val phantomRefQueue = new ReferenceQueue[File]()
  private val cleanupExecutor = new ThreadPoolExecutor(0, 1, 10, TimeUnit.MILLISECONDS, new LinkedBlockingQueue[Runnable](), new ThreadFactory {
    override def newThread(r: Runnable): Thread = {
      val thread = new Thread(r)
      thread.setDaemon(true)
      thread.setName("TransientFileCleanup")
      thread
    }
  })

  final def create(prefix: String, suffix: String, attributes: FileAttribute[_]*) : File = {
    val tempFile = Files.createTempFile(directory, prefix, suffix, attributes:_*).toFile
    registerNewTempFile(tempFile)
  }

  final def shutdown() : Unit = {
    if (this == TransientFileFactory.default){
      log.warn("Rejecting attempt to stop the default instance")
    } else {
      this.forceShutdown()
    }
  }

  private final def forceShutdown() : Unit = {
    cleanupExecutor.shutdown()
    pendingRefFiles.synchronized {
      pendingRefFiles.flatMap(_.get).foreach(_.deleteOnExit)
      pendingRefFiles.clear()
    }
    if (!cleanupExecutor.awaitTermination(50, TimeUnit.MILLISECONDS)){
      log.warn("Forcing Executor shutdown")
      cleanupExecutor.shutdownNow()
    }
  }

  private final def registerNewTempFile(file: File) : File = {
    val phantomRef = new PhantomReference[File](file, phantomRefQueue)
    log.debug(s"Registered new Transient File: ${file.getAbsolutePath}")
    pendingRefFiles.synchronized {
      if (pendingRefFiles.isEmpty){
        cleanupExecutor.execute(new CleanupPollingTask())
      }
      pendingRefFiles.add(phantomRef)
    }
    return file
  }

  private final def cleanupRegisteredRef(ref: Reference[File]) : Unit = {
    pendingRefFiles.synchronized {
      pendingRefFiles.remove(ref)
    }
    ref.get.collect {
      case file if file.exists() =>
        log.debug(s"Deleting Transient File: ${file.getAbsolutePath}")
        file.delete()
    }
    ref.clear()
  }

  private final class CleanupPollingTask extends Runnable {
    override final def run() : Unit = {
      while (!cleanupExecutor.isTerminating && pendingRefFiles.synchronized { pendingRefFiles.nonEmpty }){
        phantomRefQueue.remove.foreach(cleanupRegisteredRef)
      }
    }
  }
}

object TransientFileFactory {

  lazy val default = {
    val factory = new TransientFileFactory(FileUtils.getTempDirectory.toPath)
    Runtime.getRuntime.addShutdownHook(new Thread(new Runnable(){
      def run() : Unit = factory.forceShutdown()
    }))
    factory
  }

  final def create(prefix: String, suffix: String, attributes: FileAttribute[_]*) : File = {
    default.create(prefix, suffix, attributes:_*)
  }
}
