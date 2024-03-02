package project;

import java.io.Serializable;
import java.util.Random;

public class PaymentInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private final double discountRatio;
    private double receivedPayment;
    
    public PaymentInfo() {
        this.discountRatio = generateRandomDiscountRatio();
        this.receivedPayment = 0.0;
    }
    
    public double getDiscountRatio() {
        return discountRatio;
    }
    
    public double getReceivedPayment() {
        return receivedPayment;
    }
    
    public void increasePayment(double amount) {
        receivedPayment += amount;
    } 
    
    private double generateRandomDiscountRatio() {
        double[] possibleDiscounts = {0.0, 0.1, 0.2};
        Random random = new Random();
        int randomIndex = random.nextInt(possibleDiscounts.length);
        return possibleDiscounts[randomIndex];
    }
    
    @Override
    public String toString() {
        return "\n- Discount: %" + (discountRatio * 100) + "\n- Payment: $" + (receivedPayment - (receivedPayment * discountRatio));
    }
}
