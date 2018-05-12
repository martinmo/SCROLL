package scroll.benchmarks.bank_bigdec

import java.math.BigDecimal

class Account(var balance: BigDecimal) {
  def this(balance: Long) = this(BigDecimal.valueOf(balance))

  def increase(amount: BigDecimal): Unit = {
    //println("increase")
    balance = balance.add(amount)
  }

  def decrease(amount: BigDecimal): Unit = {
    //println("decrease")
    balance = balance.subtract(amount)
  }

  def getBalance(): BigDecimal = balance
}
