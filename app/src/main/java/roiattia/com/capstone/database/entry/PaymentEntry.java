package roiattia.com.capstone.database.entry;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import org.joda.time.LocalDate;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "payment",
        foreignKeys = {
                @ForeignKey(entity = ExpenseEntry.class, parentColumns = "expense_id",
                        childColumns = "expense_id", onDelete = CASCADE, onUpdate = CASCADE)},
        indices = {@Index("payment_id"), @Index("expense_id")})
public class PaymentEntry {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "payment_id")
    private Long mPaymentId;
    @ColumnInfo(name = "expense_id")
    private Long mExpenseId;
    @ColumnInfo(name = "payment_cost")
    private double mPaymentCost;
    @ColumnInfo(name = "payment_number")
    private int mPaymentNumber;
    @ColumnInfo(name = "payment_date")
    private LocalDate mPaymentDate;

    public PaymentEntry(Long paymentId, Long expenseId, double paymentCost,
                        int paymentNumber, LocalDate paymentDate) {
        mPaymentId = paymentId;
        mExpenseId = expenseId;
        mPaymentCost = paymentCost;
        mPaymentNumber = paymentNumber;
        mPaymentDate = paymentDate;
    }

    @Ignore
    public PaymentEntry(double paymentCost, int paymentNumber, LocalDate paymentDate) {
        mPaymentCost = paymentCost;
        mPaymentNumber = paymentNumber;
        mPaymentDate = paymentDate;
    }

    @Ignore
    public PaymentEntry() { }

    public Long getPaymentId() {
        return mPaymentId;
    }

    public void setPaymentId(Long paymentId) {
        mPaymentId = paymentId;
    }

    public Long getExpenseId() {
        return mExpenseId;
    }

    public void setExpenseId(Long expenseId) {
        mExpenseId = expenseId;
    }

    public double getPaymentCost() {
        return mPaymentCost;
    }

    public void setPaymentCost(double paymentCost) {
        mPaymentCost = paymentCost;
    }

    public int getPaymentNumber() {
        return mPaymentNumber;
    }

    public void setPaymentNumber(int paymentNumber) {
        mPaymentNumber = paymentNumber;
    }

    public LocalDate getPaymentDate() {
        return mPaymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        mPaymentDate = paymentDate;
    }

    @Override
    public String toString() {
        return "**PAYMENT ENTRY** Payment mPaymentId: " + mPaymentId +
                ", mExpenseId: " + mExpenseId +
                ", mPaymentCost: " + mPaymentCost +
                ", mPaymentNumber: " + mPaymentNumber +
                ", mPaymentDate: " + mPaymentDate;
    }
}
