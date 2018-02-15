package scroll.benchmarks.bank

class Account(var balance: Float) {
  def increase(amount: Float): Unit = {
    //println("increase")
    balance += amount
  }

  def decrease(amount: Float): Unit = {
    //println("decrease")
    balance -= amount
  }

  def getBalance(): Float = balance
}
