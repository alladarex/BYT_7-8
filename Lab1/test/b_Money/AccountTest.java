package b_Money;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
	Currency SEK, DKK;
	Bank Nordea;
	Bank DanskeBank;
	Bank SweBank;
	Account testAccount;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		SweBank.openAccount("Alice");
		testAccount = new Account("Hans", SEK);
		testAccount.deposit(new Money(10000000, SEK));

		SweBank.deposit("Alice", new Money(1000000, SEK));
	}
	
	@Test
	public void testAddRemoveTimedPayment() {
		// Adding a timed payment
        testAccount.addTimedPayment("payment1", 2, 1, new Money(5000, SEK), SweBank, "Alice");
        assertTrue(testAccount.timedPaymentExists("payment1"));

        // Removing a timed payment
        testAccount.removeTimedPayment("payment1");
        assertFalse(testAccount.timedPaymentExists("payment1"));;
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		 // Adding a timed payment
        testAccount.addTimedPayment("payment2", 2, 1, new Money(5000, SEK), SweBank, "Alice");
        System.out.println((SweBank.getBalance("Alice")));
        // Performing a few ticks to trigger the timed payment
        testAccount.tick();
        testAccount.tick();
        testAccount.tick();
        System.out.println((SweBank.getBalance("Alice")));
        // Checking the balance of Alice's account in SweBank after the timed payment
        // fail: twice the amount was transferred 
        assertEquals(new Money(1005000, SEK).getAmount(), SweBank.getBalance("Alice").getAmount()); 
        assertTrue(new Money(1005000, SEK).compareTo(SweBank.getBalance("Alice")) == 0);
  
	}

	@Test
	public void testAddWithdraw() {
	    // Adding money to the account
        testAccount.deposit(new Money(5000, SEK));
        //System.out.println(testAccount.getBalance());
        assertEquals(new Money(10005000, SEK).getAmount(), testAccount.getBalance().getAmount());

        // Withdrawing money from the account
        testAccount.withdraw(new Money(2000, SEK));
        assertEquals(new Money(10003000, SEK).getAmount(), testAccount.getBalance().getAmount());
 	}
	
	@Test
	public void testGetBalance() {
		// Checking the initial balance
        assertTrue(new Money(10000000, SEK).compareTo(testAccount.getBalance()) == 0);

        // Adding and withdrawing money to test balance changes
        testAccount.deposit(new Money(5000, SEK));
        assertTrue(new Money(10005000, SEK).compareTo(testAccount.getBalance()) == 0);

        testAccount.withdraw(new Money(2000, SEK));
        assertTrue(new Money(10003000, SEK).compareTo(testAccount.getBalance()) == 0);
    }
}
