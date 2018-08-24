package roiattia.com.capstone.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import org.joda.time.LocalDate;

@Entity(tableName = "job",
        foreignKeys = @ForeignKey(entity = CategoryEntry.class,
            parentColumns = "category_id",
            childColumns = "category_id"),
        indices = {@Index("job_id"), @Index("category_id")})
public class JobEntry {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "job_id")
    private long mJobId;
    @ColumnInfo(name = "category_id")
    private long mCategoryId;
    @ColumnInfo(name = "job_description")
    private String mJobDescription;
    @ColumnInfo(name = "job_date")
    private LocalDate mJobDate;
    @ColumnInfo(name = "job_payment_date")
    private LocalDate mJobDateOfPayment;
    @ColumnInfo(name = "job_income")
    private double mJobIncome;
    @ColumnInfo(name = "job_expenses")
    private double mJobExpenses;
    @ColumnInfo(name = "job_profits")
    private double mJobProfits;

    public JobEntry(long jobId, long categoryId, String jobDescription, LocalDate jobDate,
                    LocalDate jobDateOfPayment, double jobIncome, double jobExpenses, double jobProfits) {
        mJobId = jobId;
        mCategoryId = categoryId;
        mJobDescription = jobDescription;
        mJobDate = jobDate;
        mJobDateOfPayment = jobDateOfPayment;
        mJobIncome = jobIncome;
        mJobExpenses = jobExpenses;
        mJobProfits = jobProfits;
    }

    @Ignore
    public JobEntry(String description, LocalDate jobDate,
                    LocalDate dateOfPayment, double income, double expenses, double profit) {
        mJobDescription = description;
        mJobDate = jobDate;
        mJobDateOfPayment = dateOfPayment;
        mJobIncome = income;
        mJobExpenses = expenses;
        mJobProfits = profit;
    }

    @Ignore
    public JobEntry() {
    }

    public LocalDate getJobDate() {
        return mJobDate;
    }

    public void setJobDate(LocalDate localDate) {
        mJobDate = localDate;
    }

    public long getJobId() {
        return mJobId;
    }

    public String getJobDescription() {
        return mJobDescription;
    }

    public double getJobIncome() {
        return mJobIncome;
    }

    public double getJobExpenses() {
        return mJobExpenses;
    }

    public void setJobExpenses(double jobExpenses) {
        mJobExpenses = jobExpenses;
    }

    public double getJobProfits() {
        return mJobProfits;
    }

    public void setJobProfits(double jobProfits) {
        mJobProfits = jobProfits;
    }

    public long getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(long categoryId) {
        mCategoryId = categoryId;
    }

    public LocalDate getJobDateOfPayment() {
        return mJobDateOfPayment;
    }

    public void setJobId(long jobId) {
        mJobId = jobId;
    }

    public void setJobDescription(String jobDescription) {
        mJobDescription = jobDescription;
    }

    public void setJobDateOfPayment(LocalDate jobDateOfPayment) {
        mJobDateOfPayment = jobDateOfPayment;
    }

    public void setJobIncome(double jobIncome) {
        mJobIncome = jobIncome;
    }

    @Override
    public String toString() {
        return "**JOB ENTRY** Job mJobId: " + mJobId + ", Category mJobId: " + mCategoryId + ", job date: " + mJobDate +
                ", payment date: " + mJobDateOfPayment + ", income: " + mJobIncome +
                ", expenses: " + mJobExpenses + ", profits: " + mJobProfits +
                ", description: " + mJobDescription;
    }
}
