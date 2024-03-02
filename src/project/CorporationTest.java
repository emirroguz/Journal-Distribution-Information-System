package project;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class CorporationTest {
    private Corporation corporation;
    
    @BeforeEach
    public void setUp() throws Exception {
        corporation = new Corporation("TestCorp", "TestAddress", 1, "TestBank", 123);
    }
    
    @Test
    void testGenerateRandomBalance() {
        double balance = corporation.generateRandomBalance();
        assertTrue(balance >= 50 && balance <= 500.);
    }
    
    @Test
    void testGetBillingInformation() {
        int day = LocalDate.now().getDayOfMonth();
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        
        String expectedInfo = "Billing Information for TestCorp" + 
                              "\n- Address: TestAddress" + 
                              "\n- Bank Code: 1" + 
                              "\n- Bank Name: TestBank" + 
                              "\n- Bank Account Number: 123" + 
                              "\n- Issue Date: " + day + "/" + month + "/" + year;
        
        assertEquals(expectedInfo, corporation.getBillingInformation());
    }
}
