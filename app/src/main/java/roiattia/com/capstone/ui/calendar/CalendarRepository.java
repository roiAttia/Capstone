package roiattia.com.capstone.ui.calendar;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.database.AppDatabase;
import roiattia.com.capstone.database.dao.CategoryDao;
import roiattia.com.capstone.database.entry.CategoryEntry;
import roiattia.com.capstone.database.dao.ExpenseDao;
import roiattia.com.capstone.database.entry.ExpenseEntry;
import roiattia.com.capstone.database.dao.JobDao;
import roiattia.com.capstone.database.entry.JobEntry;
import roiattia.com.capstone.database.dao.PaymentDao;
import roiattia.com.capstone.database.entry.PaymentEntry;
import roiattia.com.capstone.model.JobCalendarModel;
import roiattia.com.capstone.database.AppExecutors;

public class CalendarRepository {
    private static final String TAG = CalendarRepository.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static CalendarRepository sInstance;
    private final JobDao mJobDao;
    private final ExpenseDao mExpenseDao;
    private final CategoryDao mCategoryDao;
    private final PaymentDao mPaymentDao;
    private final AppExecutors mExecutors;

    private CalendarRepository(JobDao jobDao, CategoryDao categoryDao, ExpenseDao expenseDao,
                               PaymentDao paymentDao, AppExecutors appExecutors){
        mJobDao = jobDao;
        mCategoryDao = categoryDao;
        mExpenseDao = expenseDao;
        mPaymentDao = paymentDao;
        mExecutors = appExecutors;
    }

    public synchronized static CalendarRepository getInstance(
            JobDao jobDao, CategoryDao categoryDao, ExpenseDao expenseDao, PaymentDao paymentDao,
            AppExecutors appExecutors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new CalendarRepository(jobDao, categoryDao, expenseDao, paymentDao, appExecutors);
            }
        }
        return sInstance;
    }

    /**
     * Get jobs with category name on a specific date
     * @param datePicked date picked
     * @return a live_data contains a list of jobs
     */
    public LiveData<List<JobCalendarModel>> getJobsByDate(final LocalDate datePicked){
        return mJobDao.loadJobsAtDate(datePicked);
    }

    public void debugPrint() {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<JobEntry> jobEntries = mJobDao.debugLoadAllJobs();
                List<ExpenseEntry> expenseEntries = mExpenseDao.debugLoadAllExpenses();
                List<CategoryEntry> categoryEntries = mCategoryDao.debugLoadCategories();
                List<PaymentEntry> paymentEntries = mPaymentDao.debugLoadPayments();
                for(JobEntry jobEntry : jobEntries){
                    Log.i(TAG, jobEntry.toString());
                }
                for(ExpenseEntry expenseEntry : expenseEntries){
                    Log.i(TAG, expenseEntry.toString());
                }
                for(PaymentEntry paymentEntry : paymentEntries){
                    Log.i(TAG, paymentEntry.toString());
                }
                for(CategoryEntry categoryEntry : categoryEntries){
                    Log.i(TAG, categoryEntry.toString());
                }
            }
        });
    }

}
