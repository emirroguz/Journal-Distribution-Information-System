package project;

import static org.junit.Assert.assertNotSame;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SubscriptionTest {
    private DateInfo dateInfo;
    private Journal journal;
    private Subscriber subscriber;
    private Subscription subscription;

    @BeforeEach
    void setUp() throws Exception {
        dateInfo = new DateInfo(1, 12, 2024);
        journal = new Journal("J1", "123", 12, 10.0);
        subscriber = new TestSubscriber("Scott", "TR");
        subscription = new Subscription(dateInfo, 1, journal, subscriber);
        subscription.increaseUnpaidCopies();
    }

    @Test
    void testGetDate() {
        assertEquals(dateInfo, subscription.getDate());
    }

    @Test
    void testGetPayment() {
        assertNotNull(subscription.getPayment());
    }

    @Test
    void testGetCopies() {
        assertEquals(1, subscription.getCopies());
    }

    @Test
    void testGetJournal() {
        assertEquals(journal, subscription.getJournal());
    }

    @Test
    void testGetSubscriber() {
        assertEquals(subscriber, subscription.getSubscriber());
    }

    @Test
    void testGetUnpaidCopies() {
        assertEquals(1, subscription.getUnpaidCopies());
    }

    @Test
    void testGetCopiesOfIssues() {
        int[] tmpArray = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        currentMonth--;
        tmpArray[currentMonth] = 1;
        assertArrayEquals(tmpArray, subscription.getCopiesOfIssues());
    }

    @Test
    void testSetCopies() {
        subscription.setCopies(6);
        assertEquals(6, subscription.getCopies());
    }

    @Test
    void testResetBalance() {
        double balance1 = subscriber.getBalance();
        subscription.increaseUnpaidCopies();
        subscription.increaseUnpaidCopies();
        subscription.increaseUnpaidCopies();
        subscription.increaseUnpaidCopies();
        subscription.increaseUnpaidCopies();
        subscription.resetBalance();
        double balance2 = subscriber.getBalance();
        assertNotSame(balance1, balance2);
    }

    @Test
    void testIsExpiring() {
        subscription = new Subscription(new DateInfo(1, 12, 2023), 1, journal, subscriber);
        assertTrue(subscription.isExpiring(12, 2023));
    }

    @Test
    void testIncreaseCopiesOfIssues() {
        subscription.increaseCopiesOfIssues();
        int[] tmpArray = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        currentMonth--;
        tmpArray[currentMonth] = 2;
        assertArrayEquals(tmpArray, subscription.getCopiesOfIssues());
    }

    @Test
    void testSetCopiesOfIssues() {
        subscription.setCopiesOfIssues(5);
        int[] tmpArray = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        currentMonth--;
        tmpArray[currentMonth] = 5;
        assertArrayEquals(tmpArray, subscription.getCopiesOfIssues());
    }

    @Test
    void testIncreaseUnpaidCopies() {
        subscription.increaseUnpaidCopies();
        assertEquals(2, subscription.getUnpaidCopies());
    }

    @Test
    void testIncreaseCopies() {
        subscription.increaseCopies();
        assertEquals(2, subscription.getCopies());
    }

    @Test
    void testAcceptPayment() {
        subscription.acceptPayment(75.0);
        assertEquals(75.0, subscription.getPayment().getReceivedPayment());
    }

    @Test
    void testIsPaymentComplete() {
        assertFalse(subscription.isPaymentComplete());
    }

    @Test
    void testCanSend() {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        assertTrue(subscription.canSend(currentMonth));
    }

    @Test
    void testToString() {
        String expectedString = "- Total Copies: 1" + subscription.getPayment() + subscription.getDate();
        assertEquals(expectedString, subscription.toString());
    }

    private static class TestSubscriber extends Subscriber {
        private static final long serialVersionUID = 1L;

        public TestSubscriber(String name, String address) {
            super(name, address);
        }

        @Override
        protected double generateRandomBalance() {
            double randomBalance = 50 + (Math.random() * 201);
            return randomBalance;
        }

        @Override
        public String getBillingInformation() {
            return "Test Billing Information";
        }
    }
}
