package project;

import java.time.LocalDate;

public class Individual extends Subscriber {
    private static final long serialVersionUID = 1L;
    private String creditCardNr;
    private int expireMonth;
    private int expireYear;
    private int CCV;
    
    public Individual(String name, String address, String creditCardNr, int expireMonth, int expireYear, int CCV) {
        super(name, address);
        this.creditCardNr = creditCardNr;
        this.expireMonth = expireMonth;
        this.expireYear = expireYear;
        this.CCV = CCV;
    }
    
    public String getCreditCardNr() {
        return creditCardNr;
    }
    
    public int getExpireMonth() {
        return expireMonth;
    }
    
    public int getExpireYear() {
        return expireYear;
    }
    
    public int getCCV() {
        return CCV;
    }
    
    public boolean cardCheck() {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();
        if (expireYear > currentYear)
            return true;
        else if (expireYear == currentYear && expireMonth >= currentMonth)
            return true;
        return false;
    }
    
    @Override
    protected double generateRandomBalance() {
        double randomBalance = 75 + (Math.random() * 101);
        return randomBalance;
    }
    
    @Override
    public String getBillingInformation() {
        return "Billing Information for " + getName() + "\n- Address: " + getAddress() + "\n- Credit Card Nr.: " + creditCardNr + 
               "\n- Expire Date: " + expireMonth + "/" + expireYear + "\n- CCV: " + CCV;
    }
}
