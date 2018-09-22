package roiattia.com.capstone.database.entry;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import org.joda.time.LocalDate;

@Entity(tableName = "expense",
        foreignKeys = {
                @ForeignKey(entity = JobEntry.class, parentColumns = "job_id",
                childColumns = "job_id"),
                @ForeignKey(entity = CategoryEntry.class, parentColumns = "category_id",
                        childColumns = "category_id")},
        indices = {@Index("expense_id"), @Index("job_id"), @Index("category_id")})
public class ExpenseEntry {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "expense_id")
    private Long mExpenseId;
    @ColumnInfo(name = "job_id")
    private Long mJobId;
    @ColumnInfo(name = "category_id")
    private Long mCategoryId;
    @ColumnInfo(name = "expense_cost")
    private double mExpenseCost;
    @ColumnInfo(name = "number_of_payments")
    private int numberOfPayments;
    @ColumnInfo(name = "expense_monthly_cost")
    private double mMonthlyCost;
    @ColumnInfo(name = "expense_first_payment_date")
    private LocalDate mExpenseFirstPayment;
    @ColumnInfo(name = "expense_last_payment_date")
    private LocalDate mExpenseLastPayment;

    @Ignore
    public ExpenseEntry(Long categoryId, double expenseCost, int numberOfPayments,
                        LocalDate expensePaymentDate) {
        this.mCategoryId = categoryId;
        this.mExpenseCost = expenseCost;
        this.numberOfPayments = numberOfPayments;
        this.mExpenseFirstPayment = expensePaymentDate;
    }

    @Ignore
    public ExpenseEntry(Long jobId, Long categoryId, double expenseCost,
                        int numberOfPayments, LocalDate expensePaymentDate) {
        this.mJobId = jobId;
        this.mCategoryId = categoryId;
        this.mExpenseCost = expenseCost;
        this.numberOfPayments = numberOfPayments;
        this.mExpenseFirstPayment = expensePaymentDate;
    }

    @Ignore
    public ExpenseEntry(Long categoryId, double cost, int numberOfPayments, double monthlyCost,
                        LocalDate expenseFirstPayment, LocalDate expenseLastPayment) {
        mCategoryId = categoryId;
        mExpenseCost = cost;
        this.numberOfPayments = numberOfPayments;
        mMonthlyCost = monthlyCost;
        mExpenseFirstPayment = expenseFirstPayment;
        mExpenseLastPayment = expenseLastPayment;
    }

    public ExpenseEntry(Long expenseId, Long jobId, Long categoryId, double expenseCost, int numberOfPayments,
                        double monthlyCost, LocalDate expenseFirstPayment, LocalDate expenseLastPayment) {
        mExpenseId = expenseId;
        mJobId = jobId;
        mCategoryId = categoryId;
        mExpenseCost = expenseCost;
        this.numberOfPayments = numberOfPayments;
        mMonthlyCost = monthlyCost;
        mExpenseFirstPayment = expenseFirstPayment;
        mExpenseLastPayment = expenseLastPayment;
    }



    public Long getExpenseId() {
        return mExpenseId;
    }

    public Long getJobId() {
        return mJobId;
    }

    public void setJobId(Long jobId) {
        this.mJobId = jobId;
    }

    public Long getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.mCategoryId = categoryId;
    }

    public int getNumberOfPayments() {
        return numberOfPayments;
    }

    public LocalDate getExpenseFirstPayment() {
        return mExpenseFirstPayment;
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

    public void setExpenseFirstPayment(LocalDate expenseFirstPayment) {
        mExpenseFirstPayment = expenseFirstPayment;
    }

    public double getMonthlyCost() {
        return mMonthlyCost;
    }

    public void setMonthlyCost(double monthlyCost) {
        mMonthlyCost = monthlyCost;
    }

    public LocalDate getExpenseLastPayment() {
        return mExpenseLastPayment;
    }

    public void setExpenseLastPayment(LocalDate expenseLastPayment) {
        mExpenseLastPayment = expenseLastPayment;
    }

    @Override
    public String toString() {
        return "**EXPENSE ENTRY** mExpenseId: " + mExpenseId +
                ", mJobId: " + mJobId + ", mCategoryId: " + mCategoryId +
                ", mExpenseCost: " + mExpenseCost + ", numberOfPayments: " + numberOfPayments +
                ", mExpenseFirstPayment: " + mExpenseFirstPayment +
                ", mExpenseLastPayment" + mExpenseLastPayment;
    }

    //TODO: add description for expense
}
