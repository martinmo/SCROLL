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
    val bank = state.bank
    for (from <- bank.getCheckingAccounts()) {
      val amount : Float = from.getBalance() / state.N
      for (to <- bank.getSavingAccounts()) {
        val transaction = new Transaction(new Source(from), new Target(to))
        transaction.execute(amount)
      }
    }
    true
  }
}
