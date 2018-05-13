package scroll.examples

import scroll.internal.Compartment

object SwallowedException extends App {
  class CoreType

  class ExceptionShowcase extends Compartment {
    class Exceptional {
      def roleMethod: Unit = {
        println("Exceptional::roleMethod()")
        throw new Exception("catch me if you can")
      }
    }
  }

  new ExceptionShowcase() {
    val core = new CoreType()
    core play new Exceptional()
    (+core).roleMethod()
    throw new AssertionError("should not be reached")
  }
}
