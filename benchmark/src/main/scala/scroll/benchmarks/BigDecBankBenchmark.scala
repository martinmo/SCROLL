package scroll.benchmarks

import java.math.BigDecimal

import org.openjdk.jmh.annotations._
import scroll.benchmarks.bank_bigdec._
import scroll.benchmarks.bank_bigdec.Transaction._

/**
  * @author Martin Morgenstern
  */
object BigDecBankBenchmark {
  @State(Scope.Benchmark)
  class Local {
    @Param(Array("250", "500", "1000"))
    var N: Int = _

    var nAsBigDecimal: BigDecimal = _

    var bank: Bank = _

    @Setup(Level.Iteration)
    def setup(): Unit = {
      bank = new Bank
      nAsBigDecimal = BigDecimal.valueOf(N)
      for (_ <- 1 to N) {
        val c = bank.newCustomer(new Person())
        c.addSavingsAccount(new Account(1000))
        c.addCheckingsAccount(new Account(1000))
      }
      Transaction partOf bank
    }
  }
}

/**
  * @author Martin Morgenstern
  */
@BenchmarkMode(Array(Mode.SingleShotTime))
@Fork(warmups = 1, value = 15)
class BigDecBankBenchmark {
  import BigDecBankBenchmark.Local

  @Benchmark
  def process_transactions_NxN(state: Local): Boolean = {
    // IMPLEMENTATION NOTE: The following loops are hand-coded using iterators, in order to get Scala
    // to emit bytecode (nearly) identical to the one that javac emits for enhanced for loops (Scala's
    // for loop translates to several lambdas). This ensures that at least the bodies of the benchmark
    // methods are comparable between the other benchmarks (RoleVM, OT/J, Role Object Pattern).

    // The only remaining difference is that state.bank and state.N are accessed with INVOKEVIRTUAL
    // instead of GETFIELD, but this happens only once:
    val bank = state.bank
    val N = state.nAsBigDecimal

    val iterator = bank.getCheckingAccounts().iterator
    while (iterator.hasNext) {
      val from = iterator.next()
      val amount = from.getBalance().divide(N)
      val innerIterator = bank.getSavingAccounts().iterator
      while (innerIterator.hasNext) {
        val to = innerIterator.next()
        val transaction = new Transaction(new Source(from), new Target(to))
        transaction.execute(amount)
      }
    }
    true
  }
}
