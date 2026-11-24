package com.raahvo.member4;

import java.time.LocalDateTime;

/**
 * Member 4: Cancellation entity representing booking cancellation records,
 * deduction calculations, and refund eligibility.
 */
public class Cancellation {
    private int cancellationId;
    private int bookingId;
    private String reason;
    private double cancellationCharge;
    private double refundEligible;
    private LocalDateTime cancelledAt;

    public Cancellation(int cancellationId, int bookingId, String reason,
                        double cancellationCharge, double refundEligible) {
        this.cancellationId = cancellationId;
        this.bookingId = bookingId;
        this.reason = reason;
        this.cancellationCharge = cancellationCharge;
        this.refundEligible = refundEligible;
        this.cancelledAt = LocalDateTime.now();
    }

    public int getCancellationId() {
        return cancellationId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getReason() {
        return reason;
    }

    public double getCancellationCharge() {
        return cancellationCharge;
    }

    public double getRefundEligible() {
        return refundEligible;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    @Override
    public String toString() {
        return String.format("Cancellation #%d | Booking #%d | Charge: ₹%.2f | Refundable: ₹%.2f | Reason: %s",
                cancellationId, bookingId, cancellationCharge, refundEligible, reason);
    }
}
