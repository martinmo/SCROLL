package scroll.benchmarks.bank

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._
import scroll.benchmarks.bank.Transaction.{Source, Target}
import scroll.benchmarks.AbstractBenchmark

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
        val p = new Person
        val c = bank.newCustomer(p)
        val sa = new Account(1000.0f)
        val ca = new Account(1000.0f)
        c.addSavingsAccount(sa)
        c.addCheckingsAccount(ca)
      }
      Transaction partOf bank
    }

  }

}

@OutputTimeUnit(TimeUnit.SECONDS)
class BankBenchmark extends AbstractBenchmark {
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
