package scroll.benchmarks.bank_bigdec

import java.math.BigDecimal

import scroll.internal.Compartment
import scroll.internal.graph.CachedScalaRoleGraph
import scroll.internal.support.DispatchQuery._

import scala.collection.mutable.ArrayBuffer

class Bank extends Compartment {
  plays = new CachedScalaRoleGraph(checkForCycles = false)
  private val checkingAccounts = ArrayBuffer.empty[Account]
  private val savingAccounts = ArrayBuffer.empty[Account]

  def getCheckingAccounts(): ArrayBuffer[Account] = checkingAccounts
  def getSavingAccounts(): ArrayBuffer[Account] = savingAccounts

  def newCustomer(person: Person): Player[Person] = {
    person play new Customer()
  }

  class Customer {
    private val accounts = ArrayBuffer.empty[Account]

    def addSavingsAccount(account: Account): Unit = {
      accounts.append(account)
      savingAccounts.append(account)
      account play new SavingsAccount()
    }

    def addCheckingsAccount(account: Account): Unit = {
      accounts.append(account)
      checkingAccounts.append(account)
      account play new CheckingsAccount()
    }
  }

  import Bank.{SavingsFee,CheckingsLimit}

  class CheckingsAccount {
    implicit val dd = Bypassing(_.isInstanceOf[CheckingsAccount])

    def decrease(amount: BigDecimal): Unit = {
      //println("decreaseWithLimit")
      if (amount.compareTo(CheckingsLimit) <= 0) {
        +this decrease amount
      } else {
        // Exceptions will be silently swallowed :(
        throw new IllegalArgumentException("amount > limit")
      }
    }
  }

  class SavingsAccount {
    implicit val dd = Bypassing(_.isInstanceOf[SavingsAccount])
    private def transactionFee(amount: BigDecimal) = amount.multiply(SavingsFee)

    def increase(amount: BigDecimal): Unit = {
      //println("increaseWithFee")
      +this increase (amount.subtract(transactionFee(amount)))
    }
  }
}

object Bank {
  private val CheckingsLimit = BigDecimal.valueOf(100)
  private val SavingsFee = new BigDecimal("0.1")
}