package project;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JournalTest {
    private DateInfo dateInfo;
    private Journal journal;
    private Subscriber subscriber;
    private Subscription subscription;
    
    @BeforeEach
    void setUp() throws Exception {
        dateInfo = new DateInfo(1, 12, 2024);
        journal = new Journal("J1", "123", 4, 10.0);
        subscriber = new TestSubscriber("Scott", "TR");
        subscription = new Subscription(dateInfo, 1, journal, subscriber);
    }
    
    @Test
    void testGetName() {
        assertEquals("J1", journal.getName());
    }
    
    @Test
    void testGetFrequency() {
        assertEquals(4, journal.getFrequency());
    }
    
    @Test
    void testGetISSN() {
        assertEquals("123", journal.getISSN());
    }
    
    @Test
    void testGetIssuePrice() {
        assertEquals(10.0, journal.getIssuePrice());
    }
    
    @Test
    void testGetSubscriptions() {
        assertNotNull(journal.getSubscriptions());
        assertTrue(journal.getSubscriptions().isEmpty());
    }
    
    @Test
    void testGetPublishingMonths() {
        assertArrayEquals(new int[]{1, 4, 7, 10}, journal.getPublishingMonths());
    }
    
    @Test
    void testAddSubscription() {
        journal.addSubscription(subscription);
        assertFalse(journal.getSubscriptions().isEmpty());
        assertEquals(1, journal.getSubscriptions().size());
    }
    
    @Test
    void testSearchSubscription() {
        journal.addSubscription(subscription);
        Subscription foundSubscription = journal.searchSubscription(subscription);
        assertNotNull(foundSubscription);
        assertEquals(subscriber, foundSubscription.getSubscriber());
        assertEquals(journal.getISSN(), foundSubscription.getJournal().getISSN());
    }
    
    @Test
    void testToString() {
        String expectedString = "- Name: J1\n- ISSN: 123\n- Frequency: 4\n- Issue Price: 10.0\n";
        assertEquals(expectedString, journal.toString());
    }
    
    @Test
    void testEquals() {
        Journal journal1 = new Journal("Sample1", "1", 1, 10.0);
        Journal journal2 = new Journal("Sample1", "1", 1, 10.0);
        assertTrue(journal1.equals(journal2));
        Journal journal3 = new Journal("Sample2", "2", 2, 20.0);
        assertFalse(journal1.equals(journal3));
        assertTrue(journal1.equals(journal1));
        assertFalse(journal1.equals(null));
    }
    
    @Test
    void testHashCode() {
        Journal journal1 = new Journal("Sample", "1", 1, 10.0);
        Journal journal2 = new Journal("Sample", "1", 1, 10.0);
        assertEquals(journal1.hashCode(), journal2.hashCode());
        Journal journal3 = new Journal("Sample2", "2", 2, 20.0);
        assertNotEquals(journal1.hashCode(), journal3.hashCode());
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
