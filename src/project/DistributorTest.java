package project;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DistributorTest {
    private Distributor distributor;
    private Journal journal;
    private Subscriber subscriber;
    
    @BeforeEach
    void setUp() throws Exception {
        distributor = new Distributor();
        journal = new Journal("J1", "123", 4, 10.0);
        subscriber = new Individual("Scott", "TR", "123", 1, 2025, 123);
    }
    
    @Test
    void testAddJournal() {
        assertTrue(distributor.addJournal(journal));
        assertFalse(distributor.addJournal(journal));
    }
    
    @Test
    void testSearchJournal() {
        distributor.addJournal(journal);
        assertEquals(journal, distributor.searchJournal("123"));
        assertNull(distributor.searchJournal("999"));
    }
    
    @Test
    void testDeleteJournal() {
        distributor.addJournal(journal);
        assertTrue(distributor.deleteJournal("123"));
        assertFalse(distributor.deleteJournal("999"));
    }
    
    @Test
    void testAddSubscriber() {
        assertTrue(distributor.addSubscriber(subscriber));
        assertFalse(distributor.addSubscriber(subscriber));
    }
    
    @Test
    void testSearchSubscriber() {
        distributor.addSubscriber(subscriber);
        assertEquals(subscriber, distributor.searchSubscriber("Scott"));
        assertNull(distributor.searchSubscriber("Ramon"));
    }
    
    @Test
    void testDeleteSubscriber() {
        distributor.addSubscriber(subscriber);
        assertTrue(distributor.deleteSubscriber("Scott"));
        assertNull(distributor.searchSubscriber("Scott"));
        assertFalse(distributor.deleteSubscriber("Ramon")); 
    }
    
    /*
    @Test
    void testAddSubscription() {
        DateInfo dateInfo = new DateInfo(1, 12, 2024);
        distributor.addJournal(journal);
        distributor.addSubscriber(subscriber);
        Subscription subscription = new Subscription(dateInfo, 1, journal, subscriber);
        assertTrue(distributor.addSubscription("123", "Scott", subscription));
    }
    */
    
    @Test
    void testSaveAndLoadState() {
        distributor.addJournal(journal);
        distributor.addSubscriber(subscriber);
        String fileName = "testState.ser";
        
        // Save State
        distributor.saveState(fileName);
        File file = new File(fileName);
        assertTrue(file.exists());
        
        // Load State
        Distributor loadedDistributor = new Distributor();
        loadedDistributor.loadState(fileName);

        assertEquals(distributor.searchJournal("123"), loadedDistributor.searchJournal("123"));
        assertEquals(distributor.searchSubscriber("Scott"), loadedDistributor.searchSubscriber("Scott"));
        
        file.delete();
    }
    
    /*
    @Test
    void testReport() {
        distributor.addJournal(journal);
        distributor.addSubscriber(subscriber);
        Subscription subscription = new Subscription(new DateInfo(1, 12, 2024), 1, journal, subscriber);
        distributor.addSubscription("123", "Scott", subscription);
        
        // Report Test
        distributor.report();
        assertFalse(distributor.isReportRunning());
        
        // Run Test
        distributor.run();
        assertFalse(distributor.isReportRunning());
    }
    */
}
