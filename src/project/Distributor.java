package project;

import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.time.LocalDate;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class Distributor implements Runnable, Serializable {
    private static final long serialVersionUID = 1L;
    private Hashtable<String, Journal> journals;
    private Vector<Subscriber> subscribers;
    private boolean reportRunning = false;
    private final Object lock = new Object();
    
    public Distributor() {
        this.journals = new Hashtable<>();
        this.subscribers = new Vector<>();
    }
    
    public Vector<Subscriber> getSubscribers() {
        return subscribers;
    }
    
    public boolean isReportRunning() {
        return reportRunning;
    }
    
    public boolean addJournal(Journal journal) {
        if (journal != null && !journals.containsKey(journal.getISSN())) {
            journals.put(journal.getISSN(), journal);
            return true;
        }
        return false;
    }
    
    public Journal searchJournal(String ISSN) {
        return journals.get(ISSN);
    }
    
    public boolean deleteJournal(String ISSN) {
        Journal journal = searchJournal(ISSN);
        if (journal != null) {
            for (Subscriber subscriber : subscribers) {
                journal.getSubscriptions().removeIf(subscription -> subscription.getSubscriber() == subscriber);
            }
            journals.remove(ISSN);
            return true;
        }
        return false;
    }
    
    public boolean addSubscriber(Subscriber subscriber) {
        if (subscriber != null && !subscribers.contains(subscriber)) {
            subscribers.add(subscriber);
            return true;
        }
        return false;
    }
    
    public Subscriber searchSubscriber(String name) {
        for (Subscriber subscriber : subscribers) {
            if (subscriber.getName().equals(name)) {
                return subscriber;
            }
        }
        return null;
    }
    
    public boolean deleteSubscriber(String name) {
        Subscriber subscriber = searchSubscriber(name);
        if (subscriber != null) {
            for (Journal journal : journals.values()) {
                journal.getSubscriptions().removeIf(subscription -> subscription.getSubscriber() == subscriber);
            }
            subscribers.remove(subscriber);
            return true;
        }
        return false;
    }
    
    public boolean addSubscription(String ISSN, String subscriberName, Subscription tmpSubscription) {
        if (ISSN != null && subscriberName != null && tmpSubscription != null) {
            Journal journal = searchJournal(ISSN);
            Subscriber subscriber = searchSubscriber(subscriberName);
            
            if (subscriber instanceof Individual) {
                if (!((Individual) subscriber).cardCheck()) {
                    JOptionPane.showMessageDialog(null, "Your credit card is expired.");
                    return false;
                }   
            }
            
            if (journal != null && subscriber != null) {
                LocalDate currentDate = LocalDate.now();
                int startMonth = currentDate.getMonthValue();
                int startYear = currentDate.getYear();
                int endMonth;
                if (startMonth == 1) {
                    endMonth = 12;
                }
                else
                    endMonth = startMonth - 1;
                
                boolean flag = false;
                int[] publishingMonths = journal.getPublishingMonths();
                for (int i = 0; i < publishingMonths.length; i++) {
                    if (startMonth == publishingMonths[i])
                        flag = true;
                }
                if (!flag) {
                    JOptionPane.showMessageDialog(null, "The journal you want to purchase does not publish a new issue this month.");
                    return false;
                }
                
                DateInfo dateInfo = new DateInfo(startMonth, endMonth, startYear);
                
                tmpSubscription = new Subscription(dateInfo, 1, journal, subscriber);
                journal.addSubscription(tmpSubscription);
                
                Subscription subscription = journal.searchSubscription(tmpSubscription);
                
                double issuePrice = journal.getIssuePrice();
                double discountRatio = subscription.getPayment().getDiscountRatio();
                issuePrice -= issuePrice * discountRatio;
                
                double balance = subscriber.getBalance();
                String balanceStr = String.format("%.1f", balance);
                
                if (balance >= issuePrice) {
                    subscriber.setBalance(balance - issuePrice);
                    subscription.acceptPayment(journal.getIssuePrice());
                    String message = "- Your balance: $" + balanceStr + 
                                     "\n- Standard issue price of the journal you want to subscribe to: $" + journal.getIssuePrice() + 
                                     "\n- Discount amount: %" + (discountRatio * 100) + 
                                     "\n- Discounted price: $" + (issuePrice) + 
                                     "\n- Payment has been made successfully!";
                    JOptionPane.showMessageDialog(null, message);
                }
                else {
                    subscription.increaseUnpaidCopies();
                    subscription.setCopies(subscription.getCopies() - 1);
                    subscription.resetBalance();
                    String message = "- Your balance: " + balanceStr + 
                                     "\n- Standard issue price of the journal you want to subscribe to: $" + journal.getIssuePrice() + 
                                     "\n- Discount ratio: %" + (discountRatio * 100) + 
                                     "\n- Discounted price: $" + (issuePrice) + 
                                     "\n- Insufficient funds. Payment failed.";
                    JOptionPane.showMessageDialog(null, message);
                    
                    int copyAmount;
                    int[] copiesOfIssues = subscription.getCopiesOfIssues();
                    for (int i = 0; i < publishingMonths.length; i++) {
                        if (startMonth == publishingMonths[i]) {
                            copyAmount = copiesOfIssues[i];
                            subscription.setCopiesOfIssues(copyAmount - 1);
                        }
                    }
                    
                }
                return true;
            }
        }
        return false;
    }
    
    public void listAllSendingOrders(int month, int year) {
        int counter = 0;
        boolean flag = false;
        
        String messageInit = "Month: " + month + ", Year: " + year + "\n\n";
        String message = messageInit;
        
        for (Subscriber subscriber : subscribers) {
            message += "Sending orders for " + subscriber.getName();
            flag = false;
            
            if(subscriber instanceof Corporation)
                message += " (Corporation Subscriber)\n";
            else
                message += " (Individual Subscriber)\n";
            
            for (Journal journal : journals.values()) {
                for (Subscription subscription : journal.getSubscriptions()) {
                    if (subscription.getSubscriber() == subscriber && subscription.canSend(month) && 
                            subscription.getDate().getStartYear() == year) {
                        counter++;
                        flag = true;
                        int[] copiesOfIssues = subscription.getCopiesOfIssues();
                        message += "- Order " + counter + "\n- Journal: " + journal.getName() + 
                                   "\n- Number of copies sent for this month's issue: " + copiesOfIssues[month - 1] + "\n\n";
                    }
                }
            }
            
            counter = 0;
            
            if (!flag)
                message += "- No orders...\n\n\n";
            else
                message += "\n";
        }
        
        if (message.equalsIgnoreCase(messageInit))
            JOptionPane.showMessageDialog(null, "Not found.");
        else {
            JTextArea textArea = setTextArea(message);
            scrollDown(textArea);
        }
    }
    
    public void listSendingOrders(String ISSN, int month, int year) {
    	Journal journal = journals.get(ISSN);
        String message = "Sending Orders for Journal " + journal.getName() + " (ISSN : " + journal.getISSN() + ")\n" + 
        				 "-> Journal Issue Price: " + journal.getIssuePrice() + "\n" + 
        				 "-> Month: " + month + ", Year: " + year + "\n\n";
        boolean flag = false;
        
        for (Subscriber subscriber : subscribers) {
            for (Subscription subscription : journal.getSubscriptions()) {
                if (subscription.getSubscriber() == subscriber && subscription.canSend(month) && subscription.getDate().getStartYear() == year) {
                	flag = true;
                	int[] copiesOfIssues = subscription.getCopiesOfIssues();
                    message += "- Subscriber: " + subscriber.getName() + 
                    		   "\n- Number of copies sent for this month's issue: " + copiesOfIssues[month - 1] + "\n\n";
                }
            }
        }
        
        if (!flag)
        	JOptionPane.showMessageDialog(null, "Not found.");
        else {
        	JTextArea textArea = setTextArea(message);
            scrollDown(textArea);
        }
    }
    
    public void listIncompletePayments() {
    	String message = "List of Subscribers with Incomplete Payments:\n\n";
        
        for (Subscriber subscriber : subscribers) {
            for (Journal journal : journals.values()) {
                for (Subscription subscription : journal.getSubscriptions()) {
                    if (subscription.getSubscriber() == subscriber && !subscription.isPaymentComplete()) {
                        message += "- Subscriber: " + subscriber.getName() + 
                        		   "\n- Journal: " + journal.getName() + 
                        		   "\n- Unpaid Copies: " + subscription.getUnpaidCopies() + 
                        		   "\n- This subscriber " +  
                        		   "had a total of " + subscription.getUnpaidCopies() + " " + 
                        		   "unsuccessful attempt(s) to purchase issue \ncopies for this journal. " + 
                        		   "The transaction could not be completed due to \ninsufficient funds.\n\n";
                    }
                }
            }
        }
        
        if (message.equalsIgnoreCase("List of Subscribers with Incomplete Payments:\n\n"))
        	JOptionPane.showMessageDialog(null, "Not found.");
        else {
        	JTextArea textArea = setTextArea(message);
            scrollDown(textArea);
        }
    }
    
    public void listSubscriptionsOfSubscriber(String subscriberName) {
    	Subscriber subscriber = searchSubscriber(subscriberName);
    	String message = "Subscriptions for Subscriber " + subscriberName + "\n\n";
        
        for (Journal journal : journals.values()) {
            for (Subscription subscription : journal.getSubscriptions()) {
                if (subscription.getSubscriber() == subscriber) {
                    message += "- Journal: " + journal.getName() + "\n- Journal Issue Price: $" + journal.getIssuePrice() + 
                    		   "\n" + subscription + "\n\n";
                }
            }
        }
        
        if (message.equalsIgnoreCase("Subscriptions for Subscriber " + subscriberName + "\n\n"))
        	JOptionPane.showMessageDialog(null, "Subscriptions not found.");
        else {
        	JTextArea textArea = setTextArea(message);
            scrollDown(textArea);
        }
    }
    
    public void listSubscriptionsOfJournal(String ISSN) {
        Journal journal = journals.get(ISSN);
        String message = "Subscriptions for " + journal.getName() + " (ISSN : " + journal.getISSN() + ")\n" + 
        				 "-> Journal Issue Price: $" + journal.getIssuePrice() + "\n\n";
        boolean flag = false;
    	
        for (Subscription subscription : journal.getSubscriptions()) {
        	flag = true;
            Subscriber subscriber = subscription.getSubscriber();
            message += "- Subscriber: " + subscriber.getName() + "\n" + subscription + "\n\n";
        }
        
        if (!flag)
        	JOptionPane.showMessageDialog(null, "Subscriptions not found.");
        else {
        	JTextArea textArea = setTextArea(message);
            scrollDown(textArea);
        }
    }
    
    public void listAllJournals() {
    	String message = "All Journals\n\n";
    	
    	for (Journal journal : journals.values()) {
    		message += journal + "\n";
    	}
        
        if (message.equalsIgnoreCase("All Journals\n\n"))
        	JOptionPane.showMessageDialog(null, "Not found.");
        else {
        	JTextArea textArea = setTextArea(message);
            JScrollPane scrollPane = new JScrollPane(textArea);
            JOptionPane.showMessageDialog(null, scrollPane, "Message", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void listAllSubscribers() {
    	String message = "All Subscribers\n\n";
    	
    	for (Subscriber subscriber : subscribers) {
			message += subscriber.getBillingInformation() + "\n\n";
		}
        
        if (message.equalsIgnoreCase("All Subscribers\n\n"))
        	JOptionPane.showMessageDialog(null, "Not found.");
        else {
        	JTextArea textArea = setTextArea(message);
            scrollDown(textArea);
        }
    }
    
    public void listAllSubscriptions() {
    	String message = "All Subscriptions\n\n";
    	boolean flag;
    	
    	for (Journal journal : journals.values()) {
    		flag = false;
    		message += "Journal: " + journal.getName() + ", Issue Price: " + journal.getIssuePrice() + "\n";
    		for (Subscription subscription : journal.getSubscriptions()) {
    			flag = true;
    			message += "- Subscriber: " + subscription.getSubscriber().getName() + "\n";
    			message += subscription + "\n\n";
			}
    		
    		if (!flag)
            	message += "- No subscriptions...\n\n\n";
            else
				message += "\n";	
    	}
    	
        if (message.equalsIgnoreCase("All Subscriptions\n\n"))
        	JOptionPane.showMessageDialog(null, "Not found.");
        else {
        	JTextArea textArea = setTextArea(message);
            scrollDown(textArea);
        }	
    }
    
    private void listExpiringSubscriptions(int month, int year) {
        String message = "Subscriptions That Will Expire After " + month + "/" + year + "\n\n";
        boolean flag = false;
        
        for (Journal journal : journals.values()) {
            for (Subscription subscription : journal.getSubscriptions()) {
                if (subscription.isExpiring(month, year)) {
                    flag = true;
                    message += "- Journal: " + journal.getName() + 
                               "\n- Subscriber: " + subscription.getSubscriber().getName() + 
                               "\n- Expiring Date: " + subscription.getDate().getEndMonth() + 
                               "/" + (subscription.getDate().getStartYear()) + " (Inclusive)\n\n";
                }
            }
        }
        
        if (!flag)
            JOptionPane.showMessageDialog(null, "No expiring subscriptions found.");
        else {
            JTextArea textArea = setTextArea(message);
            scrollDown(textArea);
        }
    }
    
    public void listReceivedAnnualPayments(int year1, int year2) {
        String messageInit = "Received Annual Payments Between " + year1 + " and " + year2 + "\n\n";
        String message = messageInit;
        boolean flag = false;
        
        for (Journal journal : journals.values()) {
            for (Subscription subscription : journal.getSubscriptions()) {
                if (subscription.getPayment().getReceivedPayment() > 0) {
                    int startMonth = subscription.getDate().getStartMonth();
                    int startYear = subscription.getDate().getStartYear();
                    int endMonth;
                    int endYear;
                    
                    if (startMonth == 1) {
                        endMonth = 12;
                        endYear = startYear;
                    }   
                    else {
                        endMonth = startMonth - 1;
                        endYear = startYear + 1;
                    }
                    
                    if (startYear >= year1 && endYear <= year2) {
                        double paymentAmount = subscription.getPayment().getReceivedPayment();
                        double discountRatio = subscription.getPayment().getDiscountRatio();
                        flag = true;
                        paymentAmount -= paymentAmount * discountRatio;
                        String paymentStr = String.format("%.2f", paymentAmount);
                        message += "- Journal: " + journal.getName() + 
                                   "\n- Subscriber: " + journal.searchSubscription(subscription).getSubscriber().getName() + 
                                   "\n- Payment Amount: $" + paymentStr +
                                   "\n- Subscription Period: " + startMonth + "/" + startYear + 
                                   " - " + endMonth + "/" + endYear + "\n\n";
                    }
                }
            }
        }
        
        if (!flag)
            JOptionPane.showMessageDialog(null, "No payments found in " + year1 + "-" + year2 + ".");
        else {
            JTextArea textArea = setTextArea(message);
            scrollDown(textArea);
        }
    }
    
    public JTextArea setTextArea(String message) {
        JTextArea textArea = new JTextArea(20, 40);
        textArea.setText(message);
        textArea.setEditable(false);
        textArea.setFont(new Font("Calibri", Font.PLAIN, 15));
        textArea.setForeground(Color.WHITE);
        textArea.setBackground(new Color(25, 50, 50));
        
        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        caret.setDot(0);
        return textArea;
    }
    
    public void scrollDown(JTextArea textArea) {
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(null, scrollPane, "Message", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public synchronized void saveState(String fileName) {
        synchronized (lock) {
            while (reportRunning) {
                try {
                    wait();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
                oos.writeObject(journals);
                oos.writeObject(subscribers);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public synchronized void loadState(String fileName) {
        synchronized (lock) {
            while (reportRunning) {
                try {
                    wait();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
                journals = (Hashtable<String, Journal>) ois.readObject();
                subscribers = (Vector<Subscriber>) ois.readObject();
            }
            catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    
    public synchronized void report() {
        reportRunning = true;
        
        try {
            LocalDate currentDate = LocalDate.now();
            int month = currentDate.getMonthValue();
            int startYear = currentDate.getYear();
            LocalDate endDate = currentDate.plusYears(1);
            int endYear = endDate.getYear();
            
            boolean flag = false;
            
            if (month == 1) {
                month = 12;
                endYear--;
                flag = true;
            }
            else
                month--;
            
            listExpiringSubscriptions(month, endYear);
            
            if (flag)
                endYear++;
            
            listReceivedAnnualPayments(startYear, endYear);
            
            Thread.sleep(100);
        }
        catch (InterruptedException | NumberFormatException e) {
            e.printStackTrace();
        }
        
        System.out.println("Report generation completed.");
        
        synchronized (lock) {
            reportRunning = false;
            notifyAll();
        }
    }
    
    @Override
    public void run() {
        System.out.println("Distributor is performing background tasks...");
        try {
            Thread.sleep(100);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        synchronized (this) {
            if (!reportRunning) {
                System.out.println("Initiating background report generation...");
                report();
            }
        }
    }
}
