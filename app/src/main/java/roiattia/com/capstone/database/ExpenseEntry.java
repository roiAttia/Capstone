package roiattia.com.capstone.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import org.joda.time.LocalDate;

@Entity(tableName = "expense",
        foreignKeys = {@ForeignKey(entity = JobEntry.class, parentColumns = "job_id",
                childColumns = "job_id"),
                @ForeignKey(entity = CategoryEntry.class, parentColumns = "category_id",
                        childColumns = "category_id")},
        indices = {@Index("expense_id"), @Index("job_id"), @Index("category_id")})
public class ExpenseEntry {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "expense_id")
    private long mExpenseId;
    @ColumnInfo(name = "job_id")
    private long mJobId;
    @ColumnInfo(name = "category_id")
    private long mCategoryId;
    @ColumnInfo(name = "expense_cost")
    private double mExpenseCost;
    @ColumnInfo(name = "number_of_payments")
    private int numberOfPayments;
    @ColumnInfo(name = "expense_payment_date")
    private LocalDate mExpensePaymentDate;

    @Ignore
    public ExpenseEntry(double expenseCost, int numberOfPayments, LocalDate expensePaymentDate) {
        this.mExpenseCost = expenseCost;
        this.numberOfPayments = numberOfPayments;
        this.mExpensePaymentDate = expensePaymentDate;
    }

    @Ignore
    public ExpenseEntry(long categoryId, double expenseCost, int numberOfPayments, LocalDate expensePaymentDate) {
        this.mCategoryId = categoryId;
        this.mExpenseCost = expenseCost;
        this.numberOfPayments = numberOfPayments;
        this.mExpensePaymentDate = expensePaymentDate;
    }

    @Ignore
    public ExpenseEntry(long jobId, long categoryId, double expenseCost,
                        int numberOfPayments, LocalDate expensePaymentDate) {
        this.mJobId = jobId;
        this.mCategoryId = categoryId;
        this.mExpenseCost = expenseCost;
        this.numberOfPayments = numberOfPayments;
        this.mExpensePaymentDate = expensePaymentDate;
    }

    ExpenseEntry(long expenseId, long jobId, long categoryId, double expenseCost,
                 int numberOfPayments, LocalDate expensePaymentDate) {
        this.mExpenseId = expenseId;
        this.mJobId = jobId;
        this.mCategoryId = categoryId;
        this.mExpenseCost = expenseCost;
        this.numberOfPayments = numberOfPayments;
        this.mExpensePaymentDate = expensePaymentDate;
    }

    public long getExpenseId() {
        return mExpenseId;
    }

    public long getJobId() {
        return mJobId;
    }

    public void setJobId(long jobId) {
        this.mJobId = jobId;
    }

    public long getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(long categoryId) {
        this.mCategoryId = categoryId;
    }

    public int getNumberOfPayments() {
        return numberOfPayments;
    }

    public LocalDate getExpensePaymentDate() {
        return mExpensePaymentDate;
    }

    public double getExpenseCost() {
        return mExpenseCost;
    }

    public void setExpenseId(long expenseId) {
        mExpenseId = expenseId;
    }

    public void setExpenseCost(double expenseCost) {
        mExpenseCost = expenseCost;
    }

    public void setNumberOfPayments(int numberOfPayments) {
        this.numberOfPayments = numberOfPayments;
    }

    public void setExpensePaymentDate(LocalDate expensePaymentDate) {
        mExpensePaymentDate = expensePaymentDate;
    }

    @Override
    public String toString() {
        return "**EXPENSE ENTRY** Expense mExpenseId: " + mExpenseId +"Job mExpenseId: " + mJobId + ", Category mExpenseId: " + mCategoryId +
                ", Cost: " + mExpenseCost + ", number of payments: " + numberOfPayments +
                ", payment day: " + mExpensePaymentDate;
    }
}
