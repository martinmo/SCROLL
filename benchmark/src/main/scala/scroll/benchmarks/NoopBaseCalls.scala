package scroll.benchmarks

import scroll.internal.Compartment
import scroll.internal.graph.CachedScalaRoleGraph

/**
  * @author Martin Morgenstern
  */
object NoopBaseCalls {
  def printTrace(e: Exception): Unit = {
    val elems = e.getStackTrace().filter(_.getClassName().startsWith("scroll.benchmarks"))
    print("Stack trace (filtered): ")
    println(elems.mkString(System.lineSeparator() + "  at "))
  }

  class BaseType() {
    def noArgs(): AnyRef = this
    def referenceArgAndReturn(o: AnyRef): AnyRef = o
    def primitiveArgsAndReturn(x: Int, y: Int): Int = {
      //printTrace(new Exception())
      x + y
    }
  }

  class NoopRoles extends Compartment {
    plays = new CachedScalaRoleGraph(checkForCycles = false)
    class NoopRoleA() {
      def noArgsA(): AnyRef = (+this).noArgs()
      def referenceArgAndReturnA(o: AnyRef): AnyRef = (+this).referenceArgAndReturn(o)
      def primitiveArgsAndReturnA(x: Int, y: Int): AnyRef = (+this).primitiveArgsAndReturn(x, y)
    }
    class NoopRoleB() {
      def noArgsB(): AnyRef = (+this).noArgsA()
      def referenceArgAndReturnB(o: AnyRef): AnyRef = (+this).referenceArgAndReturnA(o)
      def primitiveArgsAndReturnB(x: Int, y: Int): AnyRef = (+this).primitiveArgsAndReturnA(x, y)
    }
    class NoopRoleC() {
      def noArgsC(): AnyRef = (+this).noArgsB()
      def referenceArgAndReturnC(o: AnyRef): AnyRef = (+this).referenceArgAndReturnB(o)
      def primitiveArgsAndReturnC(x: Int, y: Int): AnyRef = (+this).primitiveArgsAndReturnB(x, y)
    }
    def newPlayer(numRoles: Int) = {
      val p = new BaseType()
      numRoles match {
        case 1 => p play new NoopRoleA()
        case 2 => p play new NoopRoleA() play new NoopRoleB()
        case 3 => p play new NoopRoleA() play new NoopRoleB() play new NoopRoleC()
        case _ => throw new IllegalArgumentException()
      }
    }
  }
}
