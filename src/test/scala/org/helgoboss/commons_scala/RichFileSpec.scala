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

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import java.io.File

@RunWith(classOf[JUnitRunner])
class RichFileSpec extends WordSpec with ShouldMatchers {
  "A RichFile" should {
    val absoluteWindowsFile = new RichFile(new File("""C:\Dokumente und Einstellungen\helgoboss"""))
    val absoluteWindowsFile2 = new RichFile(new File("""c:\"""))
    val absoluteCygwinFile = new RichFile(new File("/cygdrive/z/foo"))
    val absoluteUnixFile = new RichFile(new File("/opt/log/t.log"))
    val relativeFile = new RichFile(new File("f/c/.././d.hallo"))

    "fulfill all requirements" in {
      assert(absoluteWindowsFile.unixPathExpression === "C:/Dokumente und Einstellungen/helgoboss")
      assert(absoluteWindowsFile.windowsPathExpression === """C:\Dokumente und Einstellungen\helgoboss""")
      assert(absoluteWindowsFile.cygwinCompatibleUnixPathExpression === """/cygdrive/c/Dokumente und Einstellungen/helgoboss""")

      assert(absoluteWindowsFile2.unixPathExpression === "C:/")
      assert(absoluteWindowsFile2.windowsPathExpression === """C:\""")
      assert(absoluteWindowsFile2.cygwinCompatibleUnixPathExpression === """/cygdrive/c""")

      assert(absoluteCygwinFile.unixPathExpression === "Z:/foo")
      assert(absoluteCygwinFile.windowsPathExpression === """Z:\foo""")
      assert(absoluteCygwinFile.cygwinCompatibleUnixPathExpression === "/cygdrive/z/foo")

      assert(absoluteUnixFile.unixPathExpression === "/opt/log/t.log")
      assert(absoluteUnixFile.windowsPathExpression === """\opt\log\t.log""")
      assert(absoluteUnixFile.cygwinCompatibleUnixPathExpression === """/opt/log/t.log""")

      assert(relativeFile.unixPathExpression === "f/d.hallo")
      assert(relativeFile.windowsPathExpression === """f\d.hallo""")
      assert(relativeFile.cygwinCompatibleUnixPathExpression === """f/d.hallo""")

    }
  }
}
