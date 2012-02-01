package org.helgoboss.commons_scala

/**
 * Represents a class of operating systems.
 *
 * For example, class Windows includes Windows XP, Windows 7 and other Windows
 * operating systems.
 */
sealed trait OperatingSystemClass

/**
 * Contains all currently recognized operating system classes.
 */
object OperatingSystemClass {
  /** Operating system class consisting of all Microsoft Windows operating systems. */
  case object Windows extends OperatingSystemClass

  /** Operating system class consisting of all Unix-like operating systems such as Linux. */
  case object Unix extends OperatingSystemClass
}