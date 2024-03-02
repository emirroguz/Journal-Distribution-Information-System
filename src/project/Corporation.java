package project;

import java.time.LocalDate;

public class Corporation extends Subscriber {
    private static final long serialVersionUID = 1L;
    private int bankCode;
    private String bankName;
    private int accountNumber;
    private int issueDay;
    private int issueMonth;
    private int issueYear;
    
    public Corporation(String name, String address, int bankCode, String bankName, int accountNumber) {
        super(name, address);
        this.bankCode = bankCode;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.issueDay = LocalDate.now().getDayOfMonth();
        this.issueMonth = LocalDate.now().getMonthValue();
        this.issueYear = LocalDate.now().getYear(); 
    }
    
    public int getBankCode() {
        return bankCode;
    }
    
    public String getBankName() {
        return bankName;
    }
    
    public int getAccountNumber() {
        return accountNumber;
    }
    
    @Override
    protected double generateRandomBalance() {
        double randomBalance = 100 + (Math.random() * 151);
        return randomBalance;
    }
    
    @Override
    public String getBillingInformation() {
        return "Billing Information for " + getName() + "\n- Address: " + getAddress() + "\n- Bank Code: " + bankCode + 
                "\n- Bank Name: " + bankName + "\n- Bank Account Number: " + accountNumber + 
                "\n- Issue Date: " + issueDay + "/" + issueMonth + "/" + issueYear;
    }  
}
