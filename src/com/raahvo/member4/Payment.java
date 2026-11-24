package com.raahvo.member4;

import java.time.LocalDateTime;

/**
 * Member 4: Payment entity and transaction simulator.
 * Ensures PCI compliance: NEVER stores real sensitive credentials.
 */
public class Payment {
    private int paymentId;
    private int bookingId;
    private String transactionRef;
    private String paymentMethod; // UPI, CREDIT_CARD, DEBIT_CARD, NET_BANKING, WALLET
    private double amount;
    private String paymentStatus; // SUCCESS, FAILED, REFUNDED
    private LocalDateTime paymentTimestamp;

    public Payment(int paymentId, int bookingId, String transactionRef,
                   String paymentMethod, double amount, String paymentStatus) {
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.transactionRef = transactionRef;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.paymentTimestamp = LocalDateTime.now();
    }

    public int getPaymentId() {
        return paymentId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDateTime getPaymentTimestamp() {
        return paymentTimestamp;
    }

    @Override
    public String toString() {
        return String.format("TxnRef: %-22s | Method: %-12s | Amount: ₹%-8.2f | Status: %s",
                transactionRef, paymentMethod, amount, paymentStatus);
    }
}
