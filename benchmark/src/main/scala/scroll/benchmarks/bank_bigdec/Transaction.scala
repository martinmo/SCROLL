package scroll.benchmarks.bank_bigdec

import java.math.BigDecimal

import scroll.internal.Compartment

/** Source and Target are intentionally *not* implemented as role types,
  * to resemble the corresponding benchmark implementations of other
  * role runtimes as closely as possible.
  * Extending [[Compartment]] is however necessary to get the `+` operator.
  */
object Transaction extends Compartment {
  class Source(val account: Account) {
    def withdraw(amount: BigDecimal): Unit = {
      //println("withdraw")
      +account decrease(amount)
    }
  }

  class Target(val account: Account) {
    def deposit(amount: BigDecimal): Unit = {
      //println("deposit")
      +account increase(amount)
    }
  }
}

class Transaction (var from: Transaction.Source, var to: Transaction.Target) {
  def execute(amount: BigDecimal): Unit = {
    from.withdraw(amount)
    to.deposit(amount)
  }
}
