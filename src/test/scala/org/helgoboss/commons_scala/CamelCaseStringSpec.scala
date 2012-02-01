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