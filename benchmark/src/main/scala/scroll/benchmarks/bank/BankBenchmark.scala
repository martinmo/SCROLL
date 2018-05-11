package scroll.benchmarks.bank

import org.openjdk.jmh.annotations._
import scroll.benchmarks.bank.Transaction.{Source, Target}

object BankBenchmark {

  @State(Scope.Benchmark)
  class Local {
    @Param(Array("1500"))
    var N: Int = _

    var bank: Bank = _

    @Setup(Level.Iteration)
    def setup(): Unit = {
      bank = new Bank
      for (_ <- 1 to N) {
        val c = bank.newCustomer(new Person())
        c.addSavingsAccount(new Account(1000.0f))
        c.addCheckingsAccount(new Account(1000.0f))
      }
      Transaction partOf bank
    }

  }

}

@BenchmarkMode(Array(Mode.SingleShotTime))
@Fork(warmups = 1)
class BankBenchmark {
  import BankBenchmark.Local

  @Benchmark
  def process_transactions_NxN(state: Local): Boolean = {

    // IMPLEMENTATION NOTE: The following loops are hand-coded using iterators, in order to get Scala
    // to emit bytecode (nearly) identical to the one that javac emits for enhanced for loops (Scala's
    // for loop translates to several lambdas). This ensures that at least the bodies of the benchmark
    // methods are comparable between the other benchmarks (RoleVM, OT/J, Role Object Pattern).

    // The only remaining difference is that state.bank and state.N are accessed with INVOKEVIRTUAL
    // instead of GETFIELD, but this happens only once:
    val bank = state.bank
    val N = state.N

    val iterator = bank.getCheckingAccounts().iterator
    while (iterator.hasNext) {
      val from = iterator.next()
      val amount : Float = from.getBalance() / N
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
