package org.helgoboss.commons_scala

import Implicits._
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import java.io.File

@RunWith(classOf[JUnitRunner])
class CamelCaseComponentsSpec extends WordSpec with ShouldMatchers {
  "A CamelCaseComponents" should {
    val l = List("test_ 2", "my.", "word")

    "be able to make a camel case string out of the components" in {
      assert(l.camelCaseString === "test_ 2My.Word")
    }

    "should be able to reverse the CamelCaseString.camelCaseComponents method" in {
      val original = "test_ 2My.Word"
      assert(original.camelCaseComponents.camelCaseString === original)
    }
  }
}