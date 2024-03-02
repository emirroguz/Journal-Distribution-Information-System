package project;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IndividualTest {
    private Individual individual;
    
    @BeforeEach
    void setUp() throws Exception {
        individual = new Individual("Scott", "TR", "12345", 12, 2024, 123);
    }
    
    @Test
    void testGetCreditCardNr() {
        assertEquals("12345", individual.getCreditCardNr());
    }
    
    @Test
    void testGetExpireMonth() {
        assertEquals(12, individual.getExpireMonth());
    }
    
    @Test
    void testGetExpireYear() {
        assertEquals(2024, individual.getExpireYear());
    }
    
    @Test
    void testGetCCV() {
        assertEquals(123, individual.getCCV());
    }
    
    @Test
    void testCardCheckValid() {
        assertTrue(individual.cardCheck());
    }
    
    @Test
    void testCardCheckExpired() {
        individual = new Individual("ExpiredCard", "TR", "11111", 12, 2023, 111);
        assertFalse(individual.cardCheck());
    }
    
    @Test
    void testGenerateRandomBalance() {
        double balance = individual.generateRandomBalance();
        assertTrue(balance >= 50 && balance <= 250);
    }
    
    @Test
    void testGetBillingInformation() {
        String expectedInfo = "Billing Information for Scott" + 
                              "\n- Address: TR" + 
                              "\n- Credit Card Nr.: 12345" + 
                              "\n- Expire Date: 12/2024" + 
                              "\n- CCV: 123";
        
        assertEquals(expectedInfo, individual.getBillingInformation());
    }
}
