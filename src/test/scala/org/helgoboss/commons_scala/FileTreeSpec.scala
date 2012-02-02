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
import io.Source
import Implicits._

@RunWith(classOf[JUnitRunner])
class FileTreeSpec extends WordSpec with ShouldMatchers {
  "A file tree" should {
    val tree = new FileTree {
      def structure = {
        "dir1" << {
        }
        "dir2" << {
          "file1" << ""
          "file2" << "Hello world!"
          "dir3" << {
            "file3" << ""
          }
        }
      }
    }

    "create a real file tree in the file system" in {
      val tmpDir = RichFile.createTempDir(getClass.getName)
      tree.createIn(tmpDir)

      def f(path: String) = new File(tmpDir, path)

      val dir1 = f("dir1")
      val file1 = f("dir2/file1")
      val file2 = f("dir2/file2")
      val file3 = f("dir2/dir3/file3")

      assert(dir1.exists, "dir1 doesn't exist")
      assert(dir1.isDirectory, "dir1 is not a directory")

      assert(file1.exists, "file1 doesn't exist")
      assert(file1.content === "")

      assert(file2.exists, "file2 doesn't exist")
      assert(file2.content === "Hello world!")

      assert(file3.exists, "file3 doesn't exist")
      assert(file3.content === "")
    }
  }
}
