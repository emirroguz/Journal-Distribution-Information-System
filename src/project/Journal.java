package project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Journal implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String ISSN;
    private int frequency;
    private double issuePrice;
    private List<Subscription> subscriptions;
    private int[] publishingMonths;
    
    public Journal(String name, String ISSN, int frequency, double issuePrice) {
        this.name = name;
        this.ISSN = ISSN;
        this.frequency = frequency;
        this.issuePrice = issuePrice;
        this.subscriptions = new ArrayList<>();
        setPublishingMonths(frequency);
    }
    
    public String getName() {
        return name;
    }
    
    public int getFrequency() {
        return frequency;
    }
    
    public String getISSN() {
        return ISSN;
    }
    
    public double getIssuePrice() {
        return issuePrice;
    }
    
    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }
    
    public int[] getPublishingMonths() {
        return publishingMonths;
    }
    
    private void setPublishingMonths(int frequency) {
        switch (frequency) {
            case 1:
                publishingMonths = new int[]{1};
                break;
            case 2:
                publishingMonths = new int[]{1, 7};
                break;
            case 3:
                publishingMonths = new int[]{1, 5, 9};
                break;
            case 4:
                publishingMonths = new int[]{1, 4, 7, 10};
                break;
            case 6:
                publishingMonths = new int[]{1, 3, 5, 7, 9, 11};
                break;
            case 12:
                publishingMonths = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
                break;
        }
    }
    
    public void addSubscription(Subscription subscription) {
        if (subscription != null) {
            for (Subscription tmpSubscription : subscriptions) {
                if (tmpSubscription.getSubscriber().equals(subscription.getSubscriber()) && 
                        tmpSubscription.getJournal().getISSN().equalsIgnoreCase(subscription.getJournal().getISSN())) {
                    tmpSubscription.increaseCopies();
                    tmpSubscription.increaseCopiesOfIssues();
                    return;
                }   
            }
            
            subscriptions.add(subscription);
        }
    }
    
    public Subscription searchSubscription(Subscription subscription) {
        for (Subscription tmpSubscription : subscriptions) {
            if (tmpSubscription.getSubscriber().equals(subscription.getSubscriber()) && 
                    tmpSubscription.getJournal().getISSN().equalsIgnoreCase(subscription.getJournal().getISSN()))
                return tmpSubscription;
        }
        
        return null;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Journal journal = (Journal) obj;
        return Objects.equals(name, journal.name) &&
               Objects.equals(ISSN, journal.ISSN) &&
               frequency == journal.frequency &&
               Double.compare(journal.issuePrice, issuePrice) == 0;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, ISSN, frequency, issuePrice);
    }
    
    @Override
    public String toString() {
        return "- Name: " + name + "\n- ISSN: " + ISSN + "\n- Frequency: " + frequency + "\n- Issue Price: " + issuePrice + "\n";
    }
}
