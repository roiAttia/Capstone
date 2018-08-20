package roiattia.com.capstone.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import org.joda.time.LocalDate;

import java.util.Date;

@Entity(tableName = "job",
        foreignKeys = @ForeignKey(entity = CategoryEntry.class,
            parentColumns = "id",
            childColumns = "category_id"),
        indices = {@Index("id"), @Index("category_id")})
public class JobEntry {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "category_id")
    private long mCategoryId;
    private String mDescription;
    @ColumnInfo(name = "job_date")
    private LocalDate mJobDate;
    @ColumnInfo(name = "job_payment_date")
    private LocalDate mDateOfPayment;
    private double mIncome;
    private double mExpenses;
    private double mProfit;

    public JobEntry(long id, long categoryId, String description, LocalDate jobDate,
                    LocalDate dateOfPayment, double income, double expenses, double profit) {
        this.id = id;
        mCategoryId = categoryId;
        mDescription = description;
        mJobDate = jobDate;
        mDateOfPayment = dateOfPayment;
        mIncome = income;
        mExpenses = expenses;
        mProfit = profit;
    }

    @Ignore
    public JobEntry(long categoryId, String description, LocalDate jobDate,
                    LocalDate dateOfPayment, double income, double expenses, double profit) {
        mCategoryId = categoryId;
        mDescription = description;
        mJobDate = jobDate;
        mDateOfPayment = dateOfPayment;
        mIncome = income;
        mExpenses = expenses;
        mProfit = profit;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public double getIncome() {
        return mIncome;
    }

    public void setIncome(double income) {
        mIncome = income;
    }

    public double getExpenses() {
        return mExpenses;
    }

    public void setExpenses(double expenses) {
        mExpenses = expenses;
    }

    public double getProfit() {
        return mProfit;
    }

    public void setProfit(double profit) {
        mProfit = profit;
    }

    public long getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(long categoryId) {
        mCategoryId = categoryId;
    }

    public LocalDate getDateOfPayment() {
        return mDateOfPayment;
    }

    public void setDateOfPayment(LocalDate dateOfPayment) {
        mDateOfPayment = dateOfPayment;
    }

    @Override
    public String toString() {
        return "**JOB ENTRY** Job id: " + id + ", Category id: " + mCategoryId + ", job date: " + mJobDate +
                ", payment date: " + mDateOfPayment + ", income: " + mIncome +
                ", expenses: " + mExpenses + ", profits: " + mProfit +
                ", description: " + mDescription;
    }
}
