package project;

import java.io.*;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ApplicationGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private Distributor distributor;
    
    public ApplicationGUI(Distributor distributor) {
        this.distributor = distributor;
        initializeWelcomeScreen();
    }
    
    private void initializeWelcomeScreen() {
        setTitle("Journal Distribution Information System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1024, 768));
        
        JLabel welcomeLabel = new JLabel("Welcome to the Journal Distribution System!");
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        
        String imagePath = "..\\YTU_OOP_Project_Assignment\\paper.png";
        
        JButton addJournalButton = createStyledButton("Add Journal", imagePath);
        JButton deleteJournalButton = createStyledButton("Delete Journal", imagePath);
        JButton searchJournalButton = createStyledButton("Search Journal", imagePath);
        JButton addCorporationButton = createStyledButton("Add Corporation (Subscriber)",imagePath);
        JButton addIndividualButton = createStyledButton("Add Individual (Subscriber)", imagePath);
        JButton deleteSubscriberButton = createStyledButton("Delete Subscriber", imagePath);
        JButton searchSubscriberButton = createStyledButton("Search Subscriber", imagePath);
        JButton addSubscriptionButton = createStyledButton("Add Subscription", imagePath);
        JButton listAllSendingOrdersButton = createStyledButton("List All Sending Orders", imagePath);
        JButton listSendingOrdersButton = createStyledButton("List Sending Orders", imagePath);
        JButton listIncompletePaymentsButton = createStyledButton("List Incomplete Payments", imagePath);
        JButton listSubscriptionsOfSubscriberButton = createStyledButton("List Subscriptions of Subscriber", imagePath);
        JButton listSubscriptionsOfJournalButton = createStyledButton("List Subscriptions of Journal", imagePath);
        JButton listAllJournalsButton = createStyledButton("List All Journals", imagePath);
        JButton listAllSubscribersButton = createStyledButton("List All Subscribers", imagePath);
        JButton listAllSubscriptionsButton = createStyledButton("List All Subscriptions", imagePath);
        JButton reportButton = createStyledButton("Report", imagePath);
        
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(Box.createRigidArea(new Dimension(0, 20)));
        
        add(createCenteredPanel(welcomeLabel));
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(createCenteredPanel(addJournalButton));
        add(createCenteredPanel(deleteJournalButton));
        add(createCenteredPanel(searchJournalButton));
        add(createCenteredPanel(addCorporationButton));
        add(createCenteredPanel(addIndividualButton));
        add(createCenteredPanel(deleteSubscriberButton));
        add(createCenteredPanel(searchSubscriberButton));
        add(createCenteredPanel(addSubscriptionButton));
        add(createCenteredPanel(listAllSendingOrdersButton));
        add(createCenteredPanel(listSendingOrdersButton));
        add(createCenteredPanel(listIncompletePaymentsButton));
        add(createCenteredPanel(listSubscriptionsOfSubscriberButton));
        add(createCenteredPanel(listSubscriptionsOfJournalButton));
        add(createCenteredPanel(listAllJournalsButton));
        add(createCenteredPanel(listAllSubscribersButton));
        add(createCenteredPanel(listAllSubscriptionsButton));
        add(createCenteredPanel(reportButton));
        
        add(Box.createRigidArea(new Dimension(0, 25)));
        getContentPane().setBackground(new Color(5, 20, 35));
        
        addJournalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = JOptionPane.showInputDialog("Enter Journal Name:");
                    if (checkInput(name)) return;
                    
                    String ISSN = JOptionPane.showInputDialog("Enter Journal ISSN:");
                    if (checkInput(ISSN)) return;
                    else if (distributor.searchJournal(ISSN) != null) {
                        JOptionPane.showMessageDialog(null, "ISSN already exists. Please enter a unique ISSN.");
                        return;
                    }
                    
                    String frequencyStr = JOptionPane.showInputDialog("Enter Journal Frequency:");
                    if (checkInput(frequencyStr)) return;
                    int frequency = Integer.parseInt(frequencyStr);
                    if (frequency != 1 && frequency != 2 && frequency != 3 && frequency != 4 && frequency != 6 && frequency != 12) {
                        JOptionPane.showMessageDialog(null, "Invalid frequency value. " + 
                                                        "Please select one of the following values: 1, 2, 3, 4, 6, or 12.");
                        return;
                    }
                    
                    String issuePriceStr = JOptionPane.showInputDialog("Enter Journal Issue Price:");
                    if (checkInput(issuePriceStr)) return;
                    double issuePrice = Double.parseDouble(issuePriceStr);
                    if (issuePrice > 50.0) {
                        JOptionPane.showMessageDialog(null, "The price for a journal must not exceed $50.0.");
                        return;
                    }
                    
                    Journal newJournal = new Journal(name, ISSN, frequency, issuePrice);
                    
                    if (distributor.addJournal(newJournal)) {
                        JOptionPane.showMessageDialog(null, "Journal added successfully!"); 
                        distributor.saveState("data.dat");
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Failed to add journal.");
                    }
                }
                catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });
        
        deleteJournalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String ISSN = JOptionPane.showInputDialog("Enter Journal ISSN to delete:");
                    if (checkInput(ISSN)) return;
                    else if (distributor.searchJournal(ISSN) == null) {
                        JOptionPane.showMessageDialog(null, "There is no journal with this ISSN.");
                        return;
                    }
                    
                    if (distributor.deleteJournal(ISSN)) {
                        JOptionPane.showMessageDialog(null, "Journal deleted successfully!");
                        distributor.saveState("data.dat");
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Failed to delete journal.");
                    }
                }
                catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });
        
        searchJournalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String ISSN = JOptionPane.showInputDialog("Enter Journal ISSN:");
                if (checkInput(ISSN)) return;
                
                Journal searchedJournal = distributor.searchJournal(ISSN);
                
                if (searchedJournal != null) {
                    String message = "Journal Found:\n" + searchedJournal;
                    JOptionPane.showMessageDialog(null, message);
                }
                else {
                    JOptionPane.showMessageDialog(null, "Journal not found.");
                }
            }
        });
        
        addCorporationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = JOptionPane.showInputDialog("Enter Corporation Name:");
                    if (checkInput(name)) return;
                    else if (distributor.searchSubscriber(name) != null) {
                        JOptionPane.showMessageDialog(null, "This name already exists. Please enter a unique name.");
                        return;
                    }
                    
                    String address = JOptionPane.showInputDialog("Enter Corporation Address:");
                    if (checkInput(address)) return;
                    
                    String bankCodeStr = JOptionPane.showInputDialog("Enter Bank Code:");
                    if (checkInput(bankCodeStr)) return;
                    int bankCode = Integer.parseInt(bankCodeStr);
                    
                    String bankName = JOptionPane.showInputDialog("Enter Bank Name:");
                    if (checkInput(bankName)) return;
                    for (Subscriber subscriber : distributor.getSubscribers()) {
                        if (subscriber instanceof Corporation) {
                            if (((Corporation) subscriber).getBankCode() == bankCode && 
                                    !((Corporation) subscriber).getBankName().equals(bankName)) {
                                JOptionPane.showMessageDialog(null, "The bank name with this bank code is " + 
                                                                    ((Corporation) subscriber).getBankName() + 
                                                                    ". You cannot enter another bank name.");
                                return;
                            }
                        }
                    }
                    
                    String accountNumberStr = JOptionPane.showInputDialog("Enter Account Number:");
                    if (checkInput(accountNumberStr)) return;
                    int accountNumber = Integer.parseInt(accountNumberStr);
                    for (Subscriber subscriber : distributor.getSubscribers()) {
                        if (subscriber instanceof Corporation) {
                            if (((Corporation) subscriber).getBankCode() == bankCode && 
                                    ((Corporation) subscriber).getAccountNumber() == accountNumber) {
                                JOptionPane.showMessageDialog(null, "This account number belongs to someone else. " + 
                                                                    "Please try again.");
                                return;
                            }
                        }
                    }
                    
                    Corporation corporationSubscriber = new Corporation(name, address, bankCode, bankName, accountNumber);
                    
                    if (distributor.addSubscriber(corporationSubscriber)) {
                        JOptionPane.showMessageDialog(null, "Corporation subscriber added successfully!");
                        distributor.saveState("data.dat");
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Failed to add corporation subscriber.");
                    }
                }
                catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });
        
        addIndividualButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = JOptionPane.showInputDialog("Enter Individual Name:");
                    if (checkInput(name)) return;
                    else if (distributor.searchSubscriber(name) != null) {
                        JOptionPane.showMessageDialog(null, "This name already exists. Please enter a unique name.");
                        return;
                    }
                    
                    String address = JOptionPane.showInputDialog("Enter Individual Address:");
                    if (checkInput(address)) return;
                    
                    String creditCardNr = JOptionPane.showInputDialog("Enter Credit Card Number:");
                    if (checkInput(creditCardNr)) return;
                    
                    String expireMonthStr = JOptionPane.showInputDialog("Enter Credit Card Expiry Month:");
                    if (checkInput(expireMonthStr)) return;
                    
                    String expireYearStr = JOptionPane.showInputDialog("Enter Credit Card Expiry Year:");
                    if (checkInput(expireYearStr)) return;
                    
                    String ccvStr = JOptionPane.showInputDialog("Enter CCV:");
                    if (checkInput(ccvStr)) return;
                    
                    int expireMonth = Integer.parseInt(expireMonthStr);
                    int expireYear = Integer.parseInt(expireYearStr);
                    int ccv = Integer.parseInt(ccvStr);
                    
                    Individual individualSubscriber = new Individual(name, address, creditCardNr, expireMonth, expireYear, ccv);
                    
                    if (distributor.addSubscriber(individualSubscriber)) {
                        JOptionPane.showMessageDialog(null, "Individual subscriber added successfully!");
                        distributor.saveState("data.dat");
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Failed to add individual subscriber.");
                    }
                }
                catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });
        
        deleteSubscriberButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String subscriberName = JOptionPane.showInputDialog("Enter Subscriber Name to delete:");
                    if (checkInput(subscriberName)) return;
                    else if (distributor.searchSubscriber(subscriberName) == null) {
                        JOptionPane.showMessageDialog(null, "There is no subscriber named this.");
                        return;
                    }

                    if (distributor.deleteSubscriber(subscriberName)) {
                        JOptionPane.showMessageDialog(null, "Subscriber deleted successfully!");
                        distributor.saveState("data.dat");
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Failed to delete subscriber.");
                    }
                }
                catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });
        
        searchSubscriberButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String subscriberName = JOptionPane.showInputDialog("Enter Subscriber Name:");
                    if (checkInput(subscriberName)) return;
                    
                    Subscriber foundSubscriber = distributor.searchSubscriber(subscriberName);
                    if (foundSubscriber != null)
                        JOptionPane.showMessageDialog(null, "Subscriber Found:\n" + foundSubscriber.getBillingInformation());
                    else
                        JOptionPane.showMessageDialog(null, "Subscriber not found.");
                }
                catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });
        
        addSubscriptionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String ISSN = JOptionPane.showInputDialog("Enter Journal ISSN:");
                    if (checkInput(ISSN)) return;
                    else if (distributor.searchJournal(ISSN) == null) {
                        JOptionPane.showMessageDialog(null, "There is no journal with this ISSN.");
                        return;
                    }
                    Journal searchedJournal = distributor.searchJournal(ISSN);
                    
                    String subscriberName = JOptionPane.showInputDialog("Enter Subscriber Name:");
                    if (checkInput(subscriberName)) return;
                    else if (distributor.searchSubscriber(subscriberName) == null) {
                        JOptionPane.showMessageDialog(null, "There is no subscriber named this.");
                        return;
                    }
                    
                    Subscription subscription = new Subscription(null, 1, searchedJournal, null);
                    
                    if (distributor.addSubscription(ISSN, subscriberName, subscription)) {
                        distributor.saveState("data.dat");
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Failed to add subscription.");
                    }
                }
                catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });
        
        listAllSendingOrdersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {                    
                    String monthStr = JOptionPane.showInputDialog("Enter Month:");
                    if (checkInput(monthStr)) return;
                    int month = Integer.parseInt(monthStr);
                    if (month < 1 || month > 12) {
                        JOptionPane.showMessageDialog(null, "Invalid month value. The month value must be between 1 and 12.");
                        return;
                    }
                    
                    String yearStr = JOptionPane.showInputDialog("Enter Year:");
                    if (checkInput(yearStr)) return;
                    int year = Integer.parseInt(yearStr);
                    if (year < 2000) {
                        JOptionPane.showMessageDialog(null, "Invalid year value. The year value must be greater than 1999.");
                        return;
                    }
                    
                    distributor.listAllSendingOrders(month, year);
                }
                catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });
        
        listSendingOrdersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String ISSN = JOptionPane.showInputDialog("Enter Journal ISSN:");
                    if (checkInput(ISSN)) return;
                    else if (distributor.searchJournal(ISSN) == null) {
                        JOptionPane.showMessageDialog(null, "There is no journal with this ISSN.");
                        return;
                    }
                    
                    String monthStr = JOptionPane.showInputDialog("Enter Month:");
                    if (checkInput(monthStr)) return;
                    int month = Integer.parseInt(monthStr);
                    if (month < 1 || month > 12) {
                        JOptionPane.showMessageDialog(null, "Invalid month value. The month value must be between 1 and 12.");
                        return;
                    }
                    
                    String yearStr = JOptionPane.showInputDialog("Enter Year:");
                    if (checkInput(yearStr)) return;
                    int year = Integer.parseInt(yearStr);
                    if (year < 2000) {
                        JOptionPane.showMessageDialog(null, "Invalid year value. The year value must be greater than 1999.");
                        return;
                    }
                    
                    distributor.listSendingOrders(ISSN, month, year);
                }
                catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });
        
        listIncompletePaymentsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    distributor.listIncompletePayments();
                }
                catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });
        
        listSubscriptionsOfSubscriberButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String subscriberName = JOptionPane.showInputDialog("Enter Subscriber Name:");
                    if (checkInput(subscriberName)) return;
                    else if (distributor.searchSubscriber(subscriberName) == null) {
                        JOptionPane.showMessageDialog(null, "There is no subscriber named this.");
                        return;
                    }
                    distributor.listSubscriptionsOfSubscriber(subscriberName);
                }
                catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });
        
        listSubscriptionsOfJournalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String ISSN = JOptionPane.showInputDialog("Enter Journal ISSN:");
                    if (checkInput(ISSN)) return;
                    else if (distributor.searchJournal(ISSN) == null) {
                        JOptionPane.showMessageDialog(null, "There is no journal with this ISSN.");
                        return;
                    }
                    distributor.listSubscriptionsOfJournal(ISSN);
                }
                catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });
        
        listAllJournalsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    distributor.listAllJournals();
                }
                catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });
        
        listAllSubscribersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    distributor.listAllSubscribers();
                }
                catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });
        
        listAllSubscriptionsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    distributor.listAllSubscriptions();
                }
                catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });
        
        reportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Thread thread = new Thread(distributor);
                    thread.start();
                }
                catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private JButton createStyledButton(String text, String imagePath) {
        JButton button;
        
        if (imagePath != null && !imagePath.isEmpty()) {
            ImageIcon icon = new ImageIcon(imagePath);
            button = new JButton(text, icon);
        }
        else {
            button = new JButton(text);
        }
        
        MatteBorder matteBorder = new MatteBorder(2, 2, 2, 2, new Color(255, 255, 255));
        button.setBorder(matteBorder);;
        button.setBorderPainted(true);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(25, 50, 50));
        button.setPreferredSize(new Dimension(250, 25));
        button.setIconTextGap(10);
        
        return button;
    }
    
    private JPanel createCenteredPanel(Component component) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(new Color(5, 20, 35));
        panel.add(component);
        return panel;
    }
    
    public boolean checkInput(String string) {
        if (string == null) return true;
        else if (string.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a valid value.");
            return true;
        }
        return false;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Distributor distributor = new Distributor();
                
                File file = new File("data.dat");
                if (file.exists()) {
                    distributor.loadState("data.dat");
                }
                
                new ApplicationGUI(distributor);
            }
        });
    }
}
