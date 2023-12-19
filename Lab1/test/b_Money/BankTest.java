package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BankTest {
	Currency SEK, DKK;
	Bank SweBank, Nordea, DanskeBank;
	
	@Before
	public void setUp() throws Exception {
		DKK = new Currency("DKK", 0.20);
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		Nordea = new Bank("Nordea", SEK);
		DanskeBank = new Bank("DanskeBank", DKK);
		SweBank.openAccount("Ulrika");
		SweBank.openAccount("Bob");
		Nordea.openAccount("Bob");
		DanskeBank.openAccount("Gertrud");
	}

    @Test
    public void testGetName() {
        assertEquals("SweBank", SweBank.getName());
        assertEquals("Nordea", Nordea.getName());
        assertEquals("DanskeBank", DanskeBank.getName());
    }

    @Test
    public void testGetCurrency() {
        assertEquals(SEK, SweBank.getCurrency());
        assertEquals(SEK, Nordea.getCurrency());
        assertEquals(DKK, DanskeBank.getCurrency());
    }

    @Test
    public void testOpenAccount() throws AccountExistsException, AccountDoesNotExistException {
        SweBank.openAccount("Alice");
        //fail: the account isn't present in the account list
        assertTrue(SweBank.getAccountList().containsKey("Alice"));

        try {
            SweBank.openAccount("Bob");
        } catch (AccountExistsException e) {
            System.out.println("Account for Bob already exists");
        }
    }

    @Test
    public void testDeposit() throws AccountDoesNotExistException {
    	//fail: AccountDoesNotExistException is thrown 
        SweBank.deposit("Ulrika", new Money(5000, SEK));
        assertTrue(new Money(5000, SEK).compareTo(SweBank.getBalance("Ulrika")) == 0);
    }

    @Test
    public void testWithdraw() throws AccountDoesNotExistException {
        SweBank.withdraw("Bob", new Money(2000, SEK));
        //fail: expected -2000 but was 2000
        assertEquals(new Money(-2000, SEK).getAmount(), SweBank.getBalance("Bob").getAmount());
        assertTrue(new Money(-2000, SEK).compareTo(SweBank.getBalance("Bob")) == 0);
    }

    @Test
    public void testGetBalance() throws AccountDoesNotExistException {
        
        assertTrue(new Money(0, SEK).getAmount() == Nordea.getBalance("Bob").getAmount());
        try {
        	assertEquals(new Money(0, SEK).getAmount(), SweBank.getBalance("Alice"));
        } catch(AccountDoesNotExistException e) {
        	System.out.println("Account for Alice doesn't exist");
        }
        	
    }

    @Test
    public void testTransfer() throws AccountDoesNotExistException {
        SweBank.transfer("Ulrika", Nordea, "Bob", new Money(3000, SEK));
        assertTrue(new Money(-3000, SEK).compareTo(SweBank.getBalance("Ulrika")) == 0);
        assertTrue(new Money(3000, SEK).compareTo( Nordea.getBalance("Bob")) == 0);
    }

    @Test
    public void testAddTimedPayment() throws AccountDoesNotExistException {
        SweBank.addTimedPayment("Bob", "payment1", 2, 1, new Money(1000, SEK), Nordea, "Bob");
        SweBank.tick();
        SweBank.tick();
        assertTrue(new Money(-1000, SEK).compareTo(SweBank.getBalance("Bob")) == 0);
        assertTrue(new Money(1000, SEK).compareTo(Nordea.getBalance("Bob")) == 0);
    }
    
    @Test
    public void testRemoveTimedPayment() throws AccountDoesNotExistException {
    	Money before = DanskeBank.getAccountList().get("Gertrud").getBalance();
        SweBank.addTimedPayment("Bob", "payment1", 2, 1, new Money(1000, SEK), DanskeBank, "Gertrud");
        SweBank.tick();
        SweBank.removeTimedPayment("Bob", "payment1");
        SweBank.tick();
        assertTrue(before.compareTo( DanskeBank.getBalance("Gertrud")) == 0);
    }
}
