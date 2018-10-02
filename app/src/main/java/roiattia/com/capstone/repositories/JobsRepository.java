package roiattia.com.capstone.repositories;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.util.Log;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.database.AppDatabase;
import roiattia.com.capstone.database.AppExecutors;
import roiattia.com.capstone.database.entry.CategoryEntry;
import roiattia.com.capstone.database.entry.ExpenseEntry;
import roiattia.com.capstone.database.entry.JobEntry;
import roiattia.com.capstone.database.entry.PaymentEntry;
import roiattia.com.capstone.model.IncomeModel;
import roiattia.com.capstone.model.JobCalendarModel;
import roiattia.com.capstone.model.OverallIncomeModel;

public class JobsRepository {

    private static final String TAG = JobsRepository.class.getSimpleName();
    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static JobsRepository sInstance;
    private AppDatabase mDb;
    private AppExecutors mExecutors;

    private JobsRepository(Context context){
        mDb = AppDatabase.getsInstance(context);
        mExecutors = AppExecutors.getInstance();
    }

    public synchronized static JobsRepository getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new JobsRepository(context);
            }
        }
        return sInstance;
    }

    public OverallIncomeModel getIncomeAndProfitsBetweenDates(LocalDate from, LocalDate to) {
        return mDb.jobDao().loadJobsBetweenDates(from, to);
    }

    public List<IncomeModel> getIncomeModelBetweenDates(
            LocalDate currentFromDate, LocalDate expectedToDate) {
        return mDb.jobDao().loadIncomeBetweenDates(
                currentFromDate, expectedToDate, CategoryEntry.Type.JOB);
    }

    public interface OnJobListener{
        void onJobInserted(long jobId);
    }

    /**
     * Get jobs with category name on a specific date
     * @param datePicked date picked
     * @return a live_data contains a list of jobs
     */
    public List<JobCalendarModel> getJobsByDate(final LocalDate datePicked){
        return mDb.jobDao().loadJobsAtDate(datePicked);
    }

    public void debugPrint() {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<JobEntry> jobEntries = mDb.jobDao().debugLoadAllJobs();
                for(JobEntry jobEntry : jobEntries){
                    Log.i(TAG, jobEntry.toString());
                }
                List<CategoryEntry> categoryEntries = mDb.categoryDao().debugLoadCategories();
                for(CategoryEntry categoryEntry : categoryEntries){
                    Log.i(TAG, categoryEntry.toString());
                }
                List<ExpenseEntry> expenseEntries = mDb.expenseDao().debugLoadAllExpenses();
                for(ExpenseEntry expenseEntry : expenseEntries){
                    Log.i(TAG, expenseEntry.toString());
                }
                List<PaymentEntry> paymentEntries = mDb.paymentDao().debugLoadPayments();
                for(PaymentEntry paymentEntry : paymentEntries){
                    Log.i(TAG, paymentEntry.toString());
                }
            }
        });
    }

    public void deleteAllData(){
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.expenseDao().deleteAllExpenses();
                mDb.paymentDao().deleteAllPayments();
                mDb.jobDao().deleteAllJobs();
                mDb.categoryDao().deleteAllCategories();
            }
        });
    }

    public void insertJob(final JobEntry jobEntry, final OnJobListener listener){
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                long jobId = mDb.jobDao().insertJob(jobEntry);
                listener.onJobInserted(jobId);
            }
        });
    }

    public void insertJobs(final List<JobEntry> jobs) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.jobDao().insertJobs(jobs);
            }
        });
    }

    public JobEntry getJobById(long jobId) {
        return mDb.jobDao().loadJobById(jobId);
    }
}
