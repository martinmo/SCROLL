package scroll.benchmarks

import java.util.concurrent.TimeUnit
import scala.util.Random

import org.openjdk.jmh.annotations._
import scroll.benchmarks.NoopBaseCalls.NoopRoles
import scroll.internal.SCROLLDynamic

/**
  * @author Martin Morgenstern
  */
object NoopBenchmark {
  @State(Scope.Thread)
  class Local1 {
    var x, y: Int = _
    var player: SCROLLDynamic = _

    @Setup(Level.Iteration)
    def setup(): Unit = {
      player = new NoopRoles().newPlayer(numRoles = 1)
      x = Random.nextInt()
      y = Random.nextInt()
    }
  }

  @State(Scope.Thread)
  class Local2 {
    var x, y: Int = _
    var player: SCROLLDynamic = _

    @Setup(Level.Iteration)
    def setup(): Unit = {
      player = new NoopRoles().newPlayer(numRoles = 2)
      x = Random.nextInt()
      y = Random.nextInt()
    }
  }

  @State(Scope.Thread)
  class Local3 {
    var x, y: Int = _
    var player: SCROLLDynamic = _

    @Setup(Level.Iteration)
    def setup(): Unit = {
      player = new NoopRoles().newPlayer(numRoles = 3)
      x = Random.nextInt()
      y = Random.nextInt()
    }
  }
}

/** Measures role method dispatch overhead for a single role bound to
  * a player, where each role method just forwards the method call to
  * its base.
  *
  * @author Martin Morgenstern
  */
@OutputTimeUnit(TimeUnit.MICROSECONDS)
class NoopBenchmark extends AbstractBenchmark {
  import NoopBenchmark._

  @Benchmark
  def basecall_noargs(local: Local1): Any = {
    local.player.noArgsA()
  }

  @Benchmark
  def basecall_withargs(local: Local1): Any = {
    local.player.referenceArgAndReturnA(local.player)
  }

  @Benchmark
  def basecall_primitiveargs(local: Local1): Any = {
    local.player.primitiveArgsAndReturnA(local.x, local.y)
  }

  @Benchmark
  def basecall_noargs2(local: Local2): Any = {
    local.player.noArgsB()
  }

  @Benchmark
  def basecall_withargs2(local: Local2): Any = {
    local.player.referenceArgAndReturnB(local.player)
  }

  @Benchmark
  def basecall_primitiveargs2(local: Local2): Any = {
    local.player.primitiveArgsAndReturnB(local.x, local.y)
  }

  @Benchmark
  def basecall_noargs3(local: Local3): Any = {
    local.player.noArgsC()
  }

  @Benchmark
  def basecall_withargs3(local: Local3): Any = {
    local.player.referenceArgAndReturnC(local.player)
  }

  @Benchmark
  def basecall_primitiveargs3(local: Local3): Any = {
    local.player.primitiveArgsAndReturnC(local.x, local.y)
  }
}
