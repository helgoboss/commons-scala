/**
 * The MIT License
 *
 * Copyright (c) 2011 Benjamin Klum
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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