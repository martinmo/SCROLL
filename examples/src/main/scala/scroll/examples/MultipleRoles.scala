package scroll.examples

import scroll.internal.Compartment
import scroll.internal.support.DispatchQuery._

object MultipleRoles extends App {
  class CoreType {
    def someMethod(): Unit = {
      println(s"CoreType($this)::someMethod()")
    }
  }

  class MultiRole extends Compartment {
    class RoleTypeA {
      implicit val dd = Bypassing(_.equals(this)) // ???
      def someMethod(): Unit = {
        println(s"RoleTypeA($this)::someMethod()")
        (+this).someMethod()
      }
    }
    class RoleTypeB {
      implicit val dd = Bypassing(_.equals(this)) // ???
      def someMethod(): Unit = {
        println(s"RoleTypeB($this)::someMethod()")
        (+this).someMethod()
      }
    }
  }

  new MultiRole() {
    val player = new CoreType() play new RoleTypeA()
    player.someMethod()
    println("---")

    val anotherPlayer = new CoreType() play new RoleTypeA() play new RoleTypeB()
    // implicit val dd = ???
    anotherPlayer.someMethod()
    println("---")
  }
}
