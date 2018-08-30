package roiattia.com.capstone.model;

import org.joda.time.LocalDate;

public class PaymentItemModel {

    private double mCost;
    private int mPaymentNumber;
    private int mTotalPayments;
    private LocalDate mPaymentDate;

    public PaymentItemModel(double cost, int paymentNumber, int totalPayments, LocalDate paymentDate) {
        mCost = cost;
        mPaymentNumber = paymentNumber;
        mTotalPayments = totalPayments;
        mPaymentDate = paymentDate;
    }

    public double getCost() {
        return mCost;
    }

    public void setCost(double cost) {
        mCost = cost;
    }

    public int getPaymentNumber() {
        return mPaymentNumber;
    }

    public void setPaymentNumber(int paymentNumber) {
        mPaymentNumber = paymentNumber;
    }

    public int getTotalPayments() {
        return mTotalPayments;
    }

    public void setTotalPayments(int totalPayments) {
        mTotalPayments = totalPayments;
    }

    public LocalDate getPaymentDate() {
        return mPaymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        mPaymentDate = paymentDate;
    }
}
