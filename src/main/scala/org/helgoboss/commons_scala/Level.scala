package org.helgoboss.commons_scala

import java.io.File

/**
 * Represents an element in the current directory stack. Used by
 * [[org.helgoboss.commons_scala.FileTreeIterator]].
 */
trait Level {
  /**
   * Returns the `File` object pointing to the directory represented by this level.
   */
  def dir: File

  /**
   * Sets whether the directory should be further walked.
   *
   * @param value `true` if it should be further walked, `false` otherwise
   */
  def continue_=(value: Boolean)

  /**
   * Returns whether the directory will be further walked.
   *
   * @return `true` if it will be further walked, `false` otherwise
   */
  def continue: Boolean
}