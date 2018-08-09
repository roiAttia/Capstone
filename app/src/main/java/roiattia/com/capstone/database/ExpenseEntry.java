package roiattia.com.capstone.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "expense",
        foreignKeys = {@ForeignKey(entity = JobEntry.class, parentColumns = "id",
                childColumns = "job_id"),
                @ForeignKey(entity = CategoryEntry.class, parentColumns = "id",
                        childColumns = "category_id")},
        indices = {@Index("id"), @Index("job_id"), @Index("category_id")})
public class ExpenseEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "job_id")
    private int jobId;
    @ColumnInfo(name = "category_id")
    private int categoryId;
    @ColumnInfo(name = "number_of_payments")
    private int numberOfPayments;
    @ColumnInfo(name = "payment_date")
    private Date paymentDate;

    @Ignore
    public ExpenseEntry(int jobId, int categoryId, int numberOfPayments, Date paymentDate) {
        this.jobId = jobId;
        this.categoryId = categoryId;
        this.numberOfPayments = numberOfPayments;
        this.paymentDate = paymentDate;
    }

    public ExpenseEntry(int id, int jobId, int categoryId, int numberOfPayments, Date paymentDate) {
        this.id = id;
        this.jobId = jobId;
        this.categoryId = categoryId;
        this.numberOfPayments = numberOfPayments;
        this.paymentDate = paymentDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getNumberOfPayments() {
        return numberOfPayments;
    }

    public void setNumberOfPayments(int numberOfPayments) {
        this.numberOfPayments = numberOfPayments;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
}
