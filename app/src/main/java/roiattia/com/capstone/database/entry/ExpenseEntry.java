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
    @ColumnInfo(name = "description")
    private String mDescription;
    @ColumnInfo(name = "expense_cost")
    private double mExpenseCost;
    @ColumnInfo(name = "number_of_payments")
    private int mNumberOfPayments;
    @ColumnInfo(name = "expense_monthly_cost")
    private double mMonthlyCost;
    @ColumnInfo(name = "expense_first_payment_date")
    private LocalDate mExpenseFirstPayment;
    @ColumnInfo(name = "expense_last_payment_date")
    private LocalDate mExpenseLastPayment;

    @Ignore
    public ExpenseEntry(Long jobId, long categoryId, double expenseCost, String description, int numberOfPayments,
                        double monthlyCost, LocalDate firstPaymentDate, LocalDate lastPaymentDate) {
        mJobId = jobId;
        mCategoryId = categoryId;
        mDescription = description;
        mExpenseCost = expenseCost;
        mNumberOfPayments = numberOfPayments;
        mMonthlyCost = monthlyCost;
        mExpenseFirstPayment = firstPaymentDate;
        mExpenseLastPayment = lastPaymentDate;
    }

    @Ignore
    public ExpenseEntry(Long jobId, Long categoryId, double expenseCost,
                        int numberOfPayments, LocalDate firstPaymentDate, LocalDate lastPaymentDate) {
        this.mJobId = jobId;
        this.mCategoryId = categoryId;
        this.mExpenseCost = expenseCost;
        this.mNumberOfPayments = numberOfPayments;
        this.mExpenseFirstPayment = firstPaymentDate;
        mExpenseLastPayment = lastPaymentDate;
    }

    @Ignore
    public ExpenseEntry(Long categoryId, double cost, int numberOfPayments, double monthlyCost,
                        LocalDate expenseFirstPayment, LocalDate expenseLastPayment) {
        mCategoryId = categoryId;
        mExpenseCost = cost;
        this.mNumberOfPayments = numberOfPayments;
        mMonthlyCost = monthlyCost;
        mExpenseFirstPayment = expenseFirstPayment;
        mExpenseLastPayment = expenseLastPayment;
    }

    public ExpenseEntry(Long expenseId, Long jobId, Long categoryId, String description, double expenseCost,
                        int numberOfPayments, double monthlyCost, LocalDate expenseFirstPayment,
                        LocalDate expenseLastPayment) {
        mExpenseId = expenseId;
        mJobId = jobId;
        mCategoryId = categoryId;
        mDescription = description;
        mExpenseCost = expenseCost;
        this.mNumberOfPayments = numberOfPayments;
        mMonthlyCost = monthlyCost;
        mExpenseFirstPayment = expenseFirstPayment;
        mExpenseLastPayment = expenseLastPayment;
    }

    @Ignore
    public ExpenseEntry() {

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
        return mNumberOfPayments;
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
        this.mNumberOfPayments = numberOfPayments;
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

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    @Override
    public String toString() {
        return "**EXPENSE ENTRY** mExpenseId: " + mExpenseId +
                ", mJobId: " + mJobId + ", mCategoryId: " + mCategoryId +
                ", mExpenseCost: " + mExpenseCost + ", mNumberOfPayments: " + mNumberOfPayments +
                ", mExpenseFirstPayment: " + mExpenseFirstPayment +
                ", mExpenseLastPayment" + mExpenseLastPayment;
    }

}
