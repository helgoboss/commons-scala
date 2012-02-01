package org.helgoboss.commons_scala


/**
 * `String` wrapper which provides methods related to the
 * [[http://en.wikipedia.org/wiki/CamelCase camel-case]] writing style. Usually created
 * by implicit conversions.
 *
 * == Example ==
 * {{{
 * scala> import org.helgoboss.commons_scala.Implicits._
 * import org.helgoboss.commons_scala.Implicits._
 *
 * scala> "myCamelCaseString".camelCaseComponents
 * res0: Iterable[String] = ListBuffer(my, camel, case, string)
 * }}}
 *
 * @constructor Wraps the given string.
 * @param s string in camel-case writing style
 */
class CamelCaseString(s: String) {
  /**
   * Returns the camel-case components of the camel-case string.
   */
  def camelCaseComponents: Iterable[String] = {
    val currentComponent = new StringBuilder
    val components = new collection.mutable.ListBuffer[String]


    /* Closure for finishing a component. Only adds component if it's not empty. */
    def finishComponent() {
      if (!currentComponent.isEmpty) {
        components += currentComponent.toString
        currentComponent.clear()
      }
    }

    /* Iterate over characters and build components */
    s foreach {
      case upperCaseLetter if upperCaseLetter >= 'A' && upperCaseLetter <= 'Z' =>
        finishComponent()
        currentComponent += upperCaseLetter.toLower

      case other @ _ =>
        currentComponent += other
    }

    /* Finish last component */
    finishComponent()

    components
  }
}