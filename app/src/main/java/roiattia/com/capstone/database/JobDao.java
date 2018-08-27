package roiattia.com.capstone.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.model.FinancialModel;
import roiattia.com.capstone.model.IncomeModel;
import roiattia.com.capstone.model.JobCalendarModel;

@Dao
public interface JobDao {

    @Query("SELECT * FROM job ORDER BY job_profits")
    LiveData<List<JobEntry>> loadAllJobs();

    @Query("SELECT * FROM job ORDER BY job_profits")
    List<JobEntry> debugLoadAllJobs();

    @Insert
    long insertJob(JobEntry jobEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateJob(JobEntry jobEntry);

    @Delete
    void deleteJob(JobEntry jobEntry);

//    @Query("SELECT * FROM job WHERE job_date=:localDate ORDER BY job_income")
//    LiveData<List<JobEntry>> loadJobsAtDate(LocalDate localDate);

    @Query("SELECT SUM(job_income) as mIncome, SUM(job_expenses) as mExpenses," +
            " SUM(job_profits) as mProfit FROM job WHERE job_payment_date BETWEEN :from AND :to")
    LiveData<List<FinancialModel>> loadJobsBetweenDates(LocalDate from, LocalDate to);

    /**
     * Get income and profits form jobs data based on payment_date between a specific range
     * @param from the start date
     * @param to the end date
     * @param type the category type
     * @return number of transactions of each job/expense and a financial summary
     */
    @Query("SELECT COUNT(category_name) as mCount, category_name as mName, SUM(job_income) as mIncome," +
            " SUM(job_profits) as mProfit " +
            "FROM job JOIN category ON job.category_id = category.category_id " +
            "WHERE job_payment_date BETWEEN :from AND :to " +
            "AND category_type=:type " +
            "GROUP BY category_name")
    LiveData<List<IncomeModel>> loadIncomeBetweenDates(LocalDate from, LocalDate to,
                                                       CategoryEntry.Type type);

    @Query("SELECT category_name as mCategoryName, job_income as mJobIncome, " +
            "job_description as mJobDescription, job_date as mJobDate " +
            "FROM job JOIN category ON job.category_id = category.category_id " +
            "WHERE job_date=:date")
    LiveData<List<JobCalendarModel>> loadJobsAtDate(LocalDate date);

}
