package scroll.benchmarks.bank

import scroll.internal.Compartment

object Transaction extends Compartment {
  class Source(val account: Account) {
    def withdraw(amount: Float): Unit = {
      //println("withdraw")
      +account decrease(amount)
    }
  }

  class Target(val account: Account) {
    def deposit(amount: Float): Unit = {
      //println("deposit")
      +account increase(amount)
    }
  }
}

class Transaction (var from: Transaction.Source, var to: Transaction.Target) {
  def execute(amount: Float): Unit = {
    from.withdraw(amount)
    to.deposit(amount)
  }
}
