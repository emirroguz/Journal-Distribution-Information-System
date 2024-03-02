package project;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SubscriberTest {
    private Subscriber subscriber;

    @BeforeEach
    void setUp() throws Exception {
        subscriber = new TestSubscriber("Scott", "TR");
    }

    @Test
    void testGetName() {
        assertEquals("Scott", subscriber.getName());
    }

    @Test
    void testGetAddress() {
        assertEquals("TR", subscriber.getAddress());
    }

    @Test
    void testGetBalance() {
        assertTrue(subscriber.getBalance() >= 50 && subscriber.getBalance() <= 500);
    }

    @Test
    void testSetBalance() {
        subscriber.setBalance(300.0);
        assertEquals(300.0, subscriber.getBalance());
    }

    @Test
    void testGetBillingInformation() {
        String billingInfo = subscriber.getBillingInformation();
        assertNotNull(billingInfo);
    }

    @Test
    void testEquals() {
        Subscriber subscriber1 = new TestSubscriber("Travis", "123");
        Subscriber subscriber2 = new TestSubscriber("Travis", "123");
        assertTrue(subscriber1.equals(subscriber2));
        Subscriber subscriber3 = new TestSubscriber("Margot", "321");
        assertFalse(subscriber1.equals(subscriber3));
        assertTrue(subscriber1.equals(subscriber1));
        assertFalse(subscriber1.equals(null));
    }

    @Test
    void testHashCode() {
        Subscriber subscriber1 = new TestSubscriber("Travis", "123");
        Subscriber subscriber2 = new TestSubscriber("Travis", "123");
        assertEquals(subscriber1.hashCode(), subscriber2.hashCode());
        Subscriber subscriber3 = new TestSubscriber("Margot", "321");
        assertNotEquals(subscriber1.hashCode(), subscriber3.hashCode());
    }

    private static class TestSubscriber extends Subscriber {
        private static final long serialVersionUID = 1L;

        public TestSubscriber(String name, String address) {
            super(name, address);
        }

        @Override
        protected double generateRandomBalance() {
            return 100.0;
        }

        @Override
        public String getBillingInformation() {
            return "Test Billing Information";
        }
    }
}
