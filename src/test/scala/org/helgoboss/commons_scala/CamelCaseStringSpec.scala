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

import Implicits._
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import java.io.File

@RunWith(classOf[JUnitRunner])
class CamelCaseStringSpec extends WordSpec with ShouldMatchers {
  "A CamelCaseString" should {
    val s = "s__test_Pro.gram-hello  great\t TestDrive"

    "provide all the camel case components as non-capitalized strings" in {
      assert(s.camelCaseComponents.toList === List("s__test_", "pro.gram-hello  great\t ", "test", "drive"))
    }

    "should be able to reverse the CamelCaseComponents.camelCaseString method" in {
      val original = List("s__test_", "pro.gram-hello  great\t ", "test", "drive")
      assert(original.camelCaseString.camelCaseComponents.toList === original)
    }

    "allow in combination with other Scala goodies some flexible and transparent programmer's string transformations" in {
      val original = "myCamelCaseString, unfortunately withSome. dots and Whitespace inside"
      val connectedComponents = original.split("""[, .]""").filterNot(_.isEmpty).toList
      val camelCaseComponents = connectedComponents.flatMap(_.camelCaseComponents)
      val finalCamelCaseString = camelCaseComponents.camelCaseString

      assert(finalCamelCaseString === "myCamelCaseStringUnfortunatelyWithSomeDotsAndWhitespaceInside")
    }
  }
}