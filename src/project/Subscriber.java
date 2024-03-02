package project;

import java.io.Serializable;
import java.util.Objects;

public abstract class Subscriber implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String address;
    private double balance;

    public Subscriber(String name, String address) {
        this.name = name;
        this.address = address;
        this.balance = generateRandomBalance();
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    protected abstract double generateRandomBalance();

    public abstract String getBillingInformation();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Subscriber subscriber = (Subscriber) obj;
        return Objects.equals(name, subscriber.name) &&
               Objects.equals(address, subscriber.address) &&
               Double.compare(subscriber.balance, balance) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, balance);
    }
}
