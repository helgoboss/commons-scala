package org.helgoboss.commons_scala

import OperatingSystemClass._

/**
 * Provides information about the current platform on which the application is running.
 */
object CurrentPlatform {
  /**
   * Returns the operating system class.
   */
  def osClass: OperatingSystemClass = if (osName startsWith "Windows") Windows else Unix

  /**
   * Returns the name of the operating system. Just a convenience method for reading the system property
   * `os.name`.
   *
   * @return operating system name
   */
  def osName: String = System getProperty "os.name"
}