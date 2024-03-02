package project;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PaymentInfoTest {
    private PaymentInfo paymentInfo;
    
    @BeforeEach
    void setUp() throws Exception {
        paymentInfo = new PaymentInfo();
    }
    
    @Test
    void testGetReceivedPayment() {
        assertEquals(0.0, paymentInfo.getReceivedPayment());
    }
    
    @Test
    void testIncreasePayment() {
        paymentInfo.increasePayment(100.0);
        assertEquals(100.0, paymentInfo.getReceivedPayment());
    }
    
    @Test
    void testGenerateRandomDiscountRatio() {
        double discountRatio = paymentInfo.getDiscountRatio();
        assertTrue(discountRatio == 0.0 || discountRatio == 0.1 || discountRatio == 0.2);
    }
    
    @Test
    void testToString() {
        String expectedString = "\n- Discount: %" + (paymentInfo.getDiscountRatio() * 100) +
                                "\n- Payment: $" + 
                                (paymentInfo.getReceivedPayment() - (paymentInfo.getReceivedPayment() * paymentInfo.getDiscountRatio()));
        
        assertEquals(expectedString, paymentInfo.toString(), "ToString method does not match the expected result");
    }
}
