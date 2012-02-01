/**
 * The MIT License
 *
 * Copyright (c) 2010 Benjamin Klum
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

import java.io._
import io.Source
import OperatingSystemClass._

/**
 * Contains convenience methods for coping with temporary files and common system paths.
 */
object RichFile {
  /**
   * Creates and returns a temporary directory.
   *
   * @param prefix how the name of the directory should start
   * @param suffix how the name of the directory should end, defaults to an empty String
   * @return File object representing the created directory
   */
  def createTempDir(prefix: String, suffix: String = "") = {
    val tmpDir = File.createTempFile(prefix, suffix)
    tmpDir.delete()
    tmpDir.mkdir()
    tmpDir
  }

  /**
   * Works exactly like [[java.io.File]]`.createTempFile()` but you don't have to provide the suffix.
   *
   * @param prefix how the name of the file should start
   * @param suffix how the name of the file should end, defaults to an empty String
   * @return File object representing the created file
   */
  def createTempFile(prefix: String, suffix: String = "") = {
    File.createTempFile(prefix, suffix)
  }

  /**
   * Works like [[java.io.File]]`.createTempFile()` but additionally makes sure that the file doesn't exist.
   *
   * @param prefix how the name of the file should start
   * @param suffix how the name of the file should end, defaults to an empty String
   * @return File object pointing to a not yet existing file
   */
  def createNonExistingTempFile(prefix: String, suffix: String = "") = {
    val tmpFile = File.createTempFile(prefix, suffix)
    tmpFile.delete()
    tmpFile
  }

  /**
   * Returns the system directory for temporary files.
   *
   * Just wraps the value of the system property "java.io.tmpdir" in a File.
   */
  def tempDir = new File(System.getProperty("java.io.tmpdir"))

  /**
   * Returns the user's home directory.
   *
   * Just wraps the value of the system property "user.home" in a File.
   */
  def userHomeDir = new File(System.getProperty("user.home"))
}

/**
 * File wrapper which provides various convenience methods for coping with files.
 */
class RichFile(file: File) {
  /**
   * Returns a [[org.helgoboss.commons_scala.Path]] representing the file's location.
   *
   * @note [[org.helgoboss.commons_scala.Path]] don't distinguish between relative and absolute
   *  paths, so a leading slash gets lost
   */
  lazy val path = Path(file.getPath.replace("""\""", "/"))

  /**
   * Returns the path expression in Unix style.
   *
   * No file system access is made to create the expression. Symbolic links are
   * not resolved.
   *
   * A slash is used as separator. A trailing slash is inserted if the only
   * path component is `X:` where `X` is supposed to be a drive letter. Cygwin
   * paths are converted to its Windows equivalent. This path expression style
   * is appropriate for Unix-only software and Non-Cygwin Windows software
   * which prefers the slash even on Windows.
   *
   * == Example ==
   * {{{
   * scala>  import org.helgoboss.commons_scala.Implicits._
   * import org.helgoboss.commons_scala.Implicits._
   *
   * scala>  import java.io.File
   * import java.io.File
   *
   * scala>  new File("""c:\""").unixPathExpression
   * res0: java.lang.String = C:/
   *
   * scala>  new File("/cygdrive/c").unixPathExpression
   * res1: java.lang.String = C:/
   * }}}
   */
  lazy val unixPathExpression = {
    if (isAbsoluteOnUnixOrWindows) {
      /* Absolute path */
      driveLetter match {
        case Some(l) =>
          l.toUpperCase + ":/" + pathAfterDriveLetter.components.mkString("/")
        case None =>
          "/" + path.components.mkString("/")
      }
    } else {
      /* Relative path */
      path.components.mkString("/")
    }
  }

  /**
   * Returns the path expression in Windows style.
   *
   * No file system access is made to create the expression. Symbolic links are
   * not resolved.
   *
   * A backslash is used as separator. A trailing backslash is inserted if the only
   * path component is `X:` where `X` is supposed to be a drive letter. Cygwin
   * paths are converted to its Windows equivalent. This path expression style
   * is appropriate for Windows-only software and Unix software which prefers
   * the backslash, even on Unix.
   *
   * == Example ==
   * {{{
   * scala>  import org.helgoboss.commons_scala.Implicits._
   * import org.helgoboss.commons_scala.Implicits._
   *
   * scala>  import java.io.File
   * import java.io.File
   *
   * scala> new File("""C:""").windowsPathExpression
   * res0: java.lang.String = C:\
   *
   * scala> new File("""C:\Program Files""").windowsPathExpression
   * res1: java.lang.String = C:\Program Files
   *
   * scala> new File("""/cygdrive/c""").windowsPathExpression
   * res2: java.lang.String = C:\
   * }}}
   */
  lazy val windowsPathExpression = {
    if (isAbsoluteOnUnixOrWindows) {
      /* Absolute path */
      driveLetter match {
        case Some(l) =>
          l.toUpperCase + ":\\" + pathAfterDriveLetter.components.mkString("\\")
        case None =>
          "\\" + path.components.mkString("\\")
      }
    } else {
      /* Relative path */
      path.components.mkString("\\")
    }
  }


  /**
   * Returns the path expression in Unix style if the current platform is a Unix system
   * and in Windows if the current platform is a Windows system.
   *
   * Use this method for
   * passing paths to cross-platform software which prefers the native style on each system.
   * This is similar to [[java.io.File]] `.getCanonicalFile()`. However, latter doesn't care
   * about converting Cygwin paths into native Windows paths, might access the file system
   * and resolves symbolic links.
   */
  lazy val osDependentPathExpression = CurrentPlatform.osClass match {
    case Windows => windowsPathExpression
    case Unix => unixPathExpression
  }

  /**
   * Like `unixPathExpression` but converts `X:/foo` to `/cygdrive/x/foo`. Use this method
   * for passing paths to Unix software or to Windows software based on Cygwin.
   */
  lazy val cygwinCompatibleUnixPathExpression = {
    if (isAbsoluteOnUnixOrWindows) {
      /* Absolute path */
      driveLetter match {
        case Some(l) =>
          val base = "/cygdrive/" + l.toLowerCase
          if (pathAfterDriveLetter.components.isEmpty)
            base
          else
            base + "/" + pathAfterDriveLetter.components.mkString("/")

        case None => "/" + path.components.mkString("/")
      }
    } else {
      /* Relative path */
      path.components.mkString("/")
    }
  }

  /**
   * Returns a new file created by appending the given right path to this file. Should be invoked as infix operator.
   *
   * @param childPath path to append
   * @return new file
   */
  def /(childPath: Path) = new File(file, childPath.toString)

  /**
   * Writes the given String into this file overwriting any previous content.
   *
   * @param content String to write
   */
  def content_=(content: String) {
    val fw = new FileWriter(file)
    try {
      fw.write(content)
    } finally {
      fw.close()
    }
  }

  /**
   * Makes sure this file points to an empty directory. If it doesn't exist yet, the directory will be created.
   * If this file already exists, it will be deleted. If it's a directory, the complete directory tree will be
   * deleted!
   */
  def makeSureDirExistsAndIsEmpty() {
    if (file.exists)
      deleteRecursively()

    file.mkdirs()
  }

  /**
   * Makes sure this file points to a directory. If it isn't, the directory will be created. If the file already
   * exists and is no directory, an exception will be thrown.
   */
  def makeSureDirExists() {
    if (file.exists) {
      if (!file.isDirectory) {
        sys.error("File with this name is existing but is not a directory")
      }
    } else {
      file.mkdirs()
    }
  }

  /**
   * If this File object points to a directory, this methods deletes the directory recursively. If it points to a file,
   * it just deletes the file.
   *
   * @return `true` if the deletion was successful, `false` otherwise
   */
  def deleteRecursively(): Boolean = {
    def deleteFile(f: File): Boolean = {
      if (f.isDirectory) {
        f.listFiles foreach { deleteFile }
      }
      f.delete()
    }
    deleteFile(file)
  }

  /**
   * Returns the String content of this file, assuming it is a text file having platform encoding.
   */
  def content: String = {
    val source = Source.fromFile(file)

    try
      source.mkString
    finally
      source.close()
  }

  /**
   * Returns a [[org.helgoboss.commons_scala.FileTreeIterator]] over this directory. Extremely useful
   * if you want to dive into directory trees.
   *
   * @see [[org.helgoboss.commons_scala.FileTreeIterator]]
   */
  def tree = new FileTreeIterator(root = file)

  /**
   * Returns a [[org.helgoboss.commons_scala.FileTreeIterator]] over this directory that traverses
   * directory in the given default order.
   *
   * @param defaultWalkOrder order function denoting in which order to traverse files in a directory by default
   */
  def tree(defaultWalkOrder: Option[(File, File) => Boolean]) =
    new FileTreeIterator(root = file, defaultWalkOrder = defaultWalkOrder)


  private lazy val isAbsoluteOnUnixOrWindows = {
    file.getPath.startsWith("/") || file.getPath.startsWith("""\""") ||
      (path.components.headOption exists { _ contains ":" })
  }

  private case class DrivePath(driveLetter: String, remainder: Path)

  /**
   * Assumes path is absolute on unix or windows.
   */
  private def cygwinDrivePath = {
    if (path.components.size >= 2 && path.components.head == "cygdrive") {
      val CygwinDriveLetterPattern = "([a-z])".r
      path.components.tail.head match {
        case CygwinDriveLetterPattern(letter) => Some(DrivePath(letter, Path(path.components.drop(2))))
        case _ => None
      }
    } else {
      None
    }
  }

  /**
   * Assumes path is absolute on unix or windows. Returns lowercase letter.
   */
  private def windowsDrivePath = {
    if (path.components.size >= 1) {
      val WindowsDriveLetterPattern = "([A-Za-z]):".r
      path.components.head match {
        case WindowsDriveLetterPattern(letter) => Some(DrivePath(letter.toLowerCase, Path(path.components.tail)))
        case _ => None
      }
    } else {
      None
    }
  }

  private lazy val drivePath = cygwinDrivePath orElse windowsDrivePath

  private def driveLetter = drivePath map { _.driveLetter }

  private def pathAfterDriveLetter = drivePath match {
    case Some(dp) => dp.remainder
    case None => Path(Nil)
  }

}
