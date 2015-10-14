package com.harrys.util

import scala.concurrent.duration._

/**
 * Created by chris on 10/14/15.
 */
object Timer {
  def timeExecution(block: => Unit) : FiniteDuration = {
    val start = System.nanoTime()
    block
    FiniteDuration(System.nanoTime() - start, NANOSECONDS)
  }

  def timeWithResult[A](block: => A) : (FiniteDuration, A) = {
    val start     = System.nanoTime()
    val result    = block
    val duration  = FiniteDuration(System.nanoTime() - start, NANOSECONDS)
    return (duration, result)
  }

  def timeWithAttempt[A](block: => A) : (FiniteDuration, util.Try[A]) = {
    val start     = System.nanoTime()
    val attempt   = util.Try(block)
    val duration  = FiniteDuration(System.nanoTime() - start, NANOSECONDS)
    return (duration, attempt)
  }
}
