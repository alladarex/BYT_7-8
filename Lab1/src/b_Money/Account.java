package b_Money;

import java.util.Hashtable;

public class Account {
	private Money content;
	private Hashtable<String, TimedPayment> timedPayments = new Hashtable<String, TimedPayment>();

	Account(String name, Currency currency) {
		this.content = new Money(0, currency);
	}

	/**
	 * Add a timed payment
	 * @param id Id of timed payment
	 * @param interval Number of ticks between payments
	 * @param next Number of ticks till first payment
	 * @param amount Amount of Money to transfer each payment
	 * @param tobank Bank where receiving account resides
	 * @param toaccount Id of receiving account
	 */
	public void addTimedPayment(String id, Integer interval, Integer next, Money amount, Bank toBank, String toAccount) {
		TimedPayment tp = new TimedPayment(interval, next, amount, this, toBank, toAccount);
		timedPayments.put(id, tp);
    }
	
	/**
	 * Remove a timed payment
	 * @param id Id of timed payment to remove
	 */
	public void removeTimedPayment(String id) {
		timedPayments.remove(id);
	}
	
	/**
	 * Check if a timed payment exists
	 * @param id Id of timed payment to check for
	 */
	public boolean timedPaymentExists(String id) {
		return timedPayments.containsKey(id);
	}

	/**
	 * A time unit passes in the system
	 */
	public void tick() {
		/*During testing I found out the amount received was twice
		 *more than expected. It was caused by triggering tick() 
		 *method twice instead of once
		*/
		for (TimedPayment tp : timedPayments.values()) {
			tp.tick(); //tp.tick();
		}
	}
	
	/**
	 * Deposit money to the account
	 * @param money Money to deposit.
	 */
	public void deposit(Money money) {
		content = content.add(money);
		//System.out.println("added "+ money.getAmount());
	}
	
	/**
	 * Withdraw money from the account
	 * @param money Money to withdraw.
	 */
	public void withdraw(Money money) {
		content = content.sub(money);
	}

	/**
	 * Get balance of account
	 * @return Amount of Money currently on account
	 */
	public Money getBalance() {
		return content;
	}

	/* Everything below belongs to the private inner class, TimedPayment */
	private class TimedPayment {
		private int interval, next;
        private Account fromAccount;
        private Money amount;
        private Bank toBank;
        private String toAccount;
        
        TimedPayment(Integer interval, Integer next, Money amount, Account fromAccount, Bank toBank, String toAccount) {
            this.interval = interval;
            this.next = next;
            this.amount = amount;
            this.fromAccount = fromAccount;
            this.toBank = toBank;
            this.toAccount = toAccount;
        }

		/* Return value indicates whether or not a transfer was initiated */
        public Boolean tick() {
            if (next == 0) {
                next = interval;

                fromAccount.withdraw(amount);
                try {
                    toBank.deposit(toAccount, amount);
                } catch (AccountDoesNotExistException e) {
                    // Revert transfer.
                    // In reality, this should probably cause a notification somewhere.
                    fromAccount.deposit(amount);
                }
                return true;
            } else {
                next--;
                return false;
            }
        }
	}

}
