package project;

import java.io.Serializable;
import java.time.LocalDate;

public class Subscription implements Serializable {
    private static final long serialVersionUID = 1L;
    private final DateInfo date;
    private PaymentInfo payment;
    private int copies;
    private final Journal journal;
    private final Subscriber subscriber;
    private int unpaidCopies;
    private int[] copiesOfIssues = new int[12];

    public Subscription(DateInfo date, int copies, Journal journal, Subscriber subscriber) {
        this.date = date;
        this.copies = copies;
        this.journal = journal;
        this.subscriber = subscriber;
        this.payment = new PaymentInfo();
        this.unpaidCopies = 0;
        for (int i = 0; i < copiesOfIssues.length; i++) {
            copiesOfIssues[i] = 0;
        }
        setCopiesOfIssues(1);
    }

    public DateInfo getDate() {
        return date;
    }

    public PaymentInfo getPayment() {
        return payment;
    }

    public int getCopies() {
        return copies;
    }

    public Journal getJournal() {
        return journal;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public int getUnpaidCopies() {
        return unpaidCopies;
    }

    public int[] getCopiesOfIssues() {
        return copiesOfIssues;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public void resetBalance() {
        if (unpaidCopies % 5 == 0) {
            subscriber.setBalance(subscriber.generateRandomBalance());
        }
    }

    public boolean isExpiring(int month, int year) {
        if (date.getStartYear() + 1 == year && date.getStartMonth() - 1 == month)
            return true;
        else if (date.getStartYear() == year && date.getStartMonth() + 11 == month)
            return true;
        return false;
    }

    public void increaseCopiesOfIssues() {
        LocalDate currentDate = LocalDate.now();
        int month = currentDate.getMonthValue();
        int[] publishingMonths = journal.getPublishingMonths();
        for (int i = 0; i < publishingMonths.length; i++) {
            if (month == publishingMonths[i])
                copiesOfIssues[i]++;
        }
    }

    public void setCopiesOfIssues(int copyAmount) {
        LocalDate currentDate = LocalDate.now();
        int month = currentDate.getMonthValue();
        int[] publishingMonths = journal.getPublishingMonths();
        for (int i = 0; i < publishingMonths.length; i++) {
            if (month == publishingMonths[i])
                copiesOfIssues[i] = copyAmount;
        }
    }

    public void increaseUnpaidCopies() {
        unpaidCopies++;
    }

    public void increaseCopies() {
        copies++;
    }

    public void acceptPayment(double amount) {
        payment.increasePayment(amount);
    }

    public boolean isPaymentComplete() {
        return (unpaidCopies == 0);
    }

    public boolean canSend(int issueMonth) {
        return (copiesOfIssues[issueMonth - 1] > 0);
    }

    @Override
    public String toString() {
        return "- Total Copies: " + copies + payment + date;
    }
}
