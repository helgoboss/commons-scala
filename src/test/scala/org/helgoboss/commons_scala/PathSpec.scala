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
import MatchResult._

@RunWith(classOf[JUnitRunner])
class PathSpec extends WordSpec with ShouldMatchers {
  "A path" should {
    val p = Path("""/C:/dir1/dir2/../dir2/./dir3/""")
    val pp = PathPattern("C:/dir1/**")

    "should return the correct path components" in {
      assert(p.components === List("C:", "dir1", "dir2", "dir3"))
    }

    "should return the correct normalized path expression" in {
      assert(p.toString === "C:/dir1/dir2/dir3")
    }

    "should be prependable to another path" in {
      val p2 = Path("dir4/dir5")
      assert(p / p2 === Path("C:/dir1/dir2/dir3/dir4/dir5"))
    }

    "match a PathPattern if its matchAgainst method returns " + Matches in {
      assert(p matches pp)
    }

    "match a PathPattern if its matchAgainst method returns " + WillMatchEverySubPath in {
      val p2 = Path("C:/dir1")
      assert(p2 matches pp)
    }
  }
}
