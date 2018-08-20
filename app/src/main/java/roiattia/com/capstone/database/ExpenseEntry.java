package roiattia.com.capstone.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import org.joda.time.LocalDate;

import java.util.Date;

@Entity(tableName = "expense",
        foreignKeys = {@ForeignKey(entity = JobEntry.class, parentColumns = "id",
                childColumns = "job_id"),
                @ForeignKey(entity = CategoryEntry.class, parentColumns = "id",
                        childColumns = "category_id")},
        indices = {@Index("id"), @Index("job_id"), @Index("category_id")})
public class ExpenseEntry {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "job_id")
    private long jobId;
    @ColumnInfo(name = "category_id")
    private long categoryId;
    private double cost;
    @ColumnInfo(name = "number_of_payments")
    private int numberOfPayments;
    @ColumnInfo(name = "payment_date")
    private LocalDate paymentDate;

    @Ignore
    public ExpenseEntry(double cost, int numberOfPayments, LocalDate paymentDate) {
        this.cost = cost;
        this.numberOfPayments = numberOfPayments;
        this.paymentDate = paymentDate;
    }

    @Ignore
    public ExpenseEntry(long categoryId, double cost, int numberOfPayments, LocalDate paymentDate) {
        this.categoryId = categoryId;
        this.cost = cost;
        this.numberOfPayments = numberOfPayments;
        this.paymentDate = paymentDate;
    }

    @Ignore
    public ExpenseEntry(long jobId, long categoryId, double cost,
                        int numberOfPayments, LocalDate paymentDate) {
        this.jobId = jobId;
        this.categoryId = categoryId;
        this.cost = cost;
        this.numberOfPayments = numberOfPayments;
        this.paymentDate = paymentDate;
    }

    public ExpenseEntry(long id, long jobId, long categoryId, double cost,
                        int numberOfPayments, LocalDate paymentDate) {
        this.id = id;
        this.jobId = jobId;
        this.categoryId = categoryId;
        this.cost = cost;
        this.numberOfPayments = numberOfPayments;
        this.paymentDate = paymentDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public int getNumberOfPayments() {
        return numberOfPayments;
    }

    public void setNumberOfPayments(int numberOfPayments) {
        this.numberOfPayments = numberOfPayments;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "**EXPENSE ENTRY** Expense id: " + id +"Job id: " + jobId + ", Category id: " + categoryId +
                ", Cost: " + cost + ", number of payments: " + numberOfPayments +
                ", payment day: " + paymentDate;
    }
}
