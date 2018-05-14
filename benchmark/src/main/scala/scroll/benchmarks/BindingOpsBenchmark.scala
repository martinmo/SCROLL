package scroll.benchmarks

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._
import org.openjdk.jmh.infra.Blackhole
import scroll.internal.Compartment

class BaseType

/**
  * The bindAll and unbindAll ops had to be implemented in a separate
  * class, because we cannot import the implicit play and drop methods
  * in JMH benchmarks.
  *
  * @author Martin Morgenstern
  */
class SimpleCompartment(numRoles: Int) extends Compartment {
  val base = new BaseType()
  val roles = initRoles()

  class RoleType

  def initRoles() = {
    val roles = new Array[RoleType](numRoles)
    for (i <- 0 until numRoles) {
      roles(i) = new RoleType()
    }
    roles
  }

  // we use Array of roles plus simple loop, so we don't
  // measure the cost of the Scala "for" loop

  def bindAll(bh: Blackhole): Unit = {
    var i = 0
    while (i < numRoles) {
      bh.consume(base play roles(i))
      i += 1
    }
  }

  def unbindAll(bh: Blackhole): Unit = {
    var i = 0
    while (i < numRoles) {
      bh.consume(base drop roles(i))
      i += 1
    }
  }
}

/**
  * @author Martin Morgenstern
  */
object BindingOpsBenchmark {
  @State(Scope.Benchmark)
  class Local {
    @Param(Array("5"))
    var N: Int = _

    var compartment: SimpleCompartment = _

    @Setup(Level.Iteration)
    def setup(): Unit = {
      compartment = new SimpleCompartment(N)
    }

    @TearDown(Level.Iteration)
    def tearDown() {
      // may help when we use JMH's -gc flag:
      compartment = null
    }
  }
}

/**
  * @author Martin Morgenstern
  */
@OutputTimeUnit(TimeUnit.MICROSECONDS)
class BindingOpsBenchmark extends AbstractBenchmark {
  import BindingOpsBenchmark.Local

  @Benchmark
  def bind_unbind_N_roles(local: Local, bh: Blackhole): Unit = {
    local.compartment.bindAll(bh)
    local.compartment.unbindAll(bh)
  }
}
