package org.helgoboss.commons_scala

/**
 * Provides a common interface for logging in Scala. Uses Scala's "call by name" possibility so you don't have to
 * check whether the desired log level is enabled or not. Implementations should make sure that the arguments
 * are evaluated only if the corresponding log level is enabled.
 */
trait Logger {
  /**
   * Logs the given message on debug level.
   */
  def debug(message: => AnyRef)

  /**
   * Logs the given message and exception on debug level.
   */
  def debug(message: => AnyRef, exception: => Throwable)

  /**
   * Logs the given message on info level.
   */
  def info(message: => AnyRef)

  /**
   * Logs the given message and exception on info level.
   */
  def info(message: => AnyRef, exception: => Throwable)

  /**
   * Logs the given message on warn level.
   */
  def warn(message: => AnyRef)

  /**
   * Logs the given message and exception on warn level.
   */
  def warn(message: => AnyRef, exception: => Throwable)

  /**
   * Logs the given message on error level.
   */
  def error(message: => AnyRef)

  /**
   * Logs the given message and exception on error level.
   */
  def error(message: => AnyRef, exception: => Throwable)

  /**
   * Logs the given message on trace level.
   */
  def trace(message: => AnyRef)

  /**
   * Logs the given message and exception on trace level.
   */
  def trace(message: => AnyRef, exception: => Throwable)
}