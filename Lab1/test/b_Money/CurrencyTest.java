package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CurrencyTest {
    Currency SEK, DKK, NOK, EUR;

    @Before
    public void setUp() throws Exception {
        /* Setup currencies with exchange rates */
        SEK = new Currency("SEK", 0.15);
        DKK = new Currency("DKK", 0.20);
        EUR = new Currency("EUR", 1.5);
    }

    @Test
    public void testGetName() {
        assertEquals("SEK", SEK.getName());
        assertEquals("DKK", DKK.getName());
        assertEquals("EUR", EUR.getName());
    }

    @Test
    public void testGetRate() {
        assertEquals(0.15, SEK.getRate(), 0);
        assertEquals(0.20, DKK.getRate(), 0);
        assertEquals(1.5, EUR.getRate(), 0);
    }

    @Test
    public void testSetRate() {
        SEK.setRate(0.25);
        assertEquals(0.25, SEK.getRate(), 0);
        
        DKK.setRate(0.30);
        assertEquals(0.30, DKK.getRate(), 0);
        
        EUR.setRate(1.8);
        assertEquals(1.8, EUR.getRate(), 0);
    }

    @Test
    public void testGlobalValue() {
        assertEquals(Integer.valueOf(150), SEK.universalValue(1000));
        assertEquals(Integer.valueOf(200), DKK.universalValue(1000));
        assertEquals(Integer.valueOf(1500), EUR.universalValue(1000));
    }

    @Test
    public void testValueInThisCurrency() {
        assertEquals(Integer.valueOf(1000), SEK.valueInThisCurrency(100, EUR));
        assertEquals(Integer.valueOf(750), DKK.valueInThisCurrency(1000, SEK));
        assertEquals(Integer.valueOf(100), EUR.valueInThisCurrency(1000, SEK));
    }
}
