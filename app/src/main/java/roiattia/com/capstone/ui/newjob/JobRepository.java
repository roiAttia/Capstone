package roiattia.com.capstone.ui.newjob;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.List;

import roiattia.com.capstone.database.CategoryDao;
import roiattia.com.capstone.database.CategoryEntry;
import roiattia.com.capstone.database.ExpenseDao;
import roiattia.com.capstone.database.ExpenseEntry;
import roiattia.com.capstone.database.JobDao;
import roiattia.com.capstone.database.JobEntry;
import roiattia.com.capstone.ui.newexpense.ExpenseRepository;
import roiattia.com.capstone.utils.AppExecutors;

public class JobRepository {

    private static final String TAG = JobRepository.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static JobRepository sInstance;
    private final JobDao mJobDao;
    private final CategoryDao mCategoryDao;
    private final ExpenseDao mExpenseDao;
    private final AppExecutors mExecutors;
    private GetJobIdHandler mCallback;

    private JobRepository(JobDao jobDao, CategoryDao categoryDao, ExpenseDao expenseDao,
                          AppExecutors appExecutors){
        mJobDao = jobDao;
        mCategoryDao = categoryDao;
        mExpenseDao = expenseDao;
        mExecutors = appExecutors;
    }

    public synchronized static JobRepository getInstance(
            JobDao jobDao, CategoryDao categoryDao, ExpenseDao expenseDao,
            AppExecutors appExecutors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new JobRepository(jobDao, categoryDao, expenseDao, appExecutors);
            }
        }
        return sInstance;
    }

    public void insertJob(final JobEntry jobEntry) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                long jobId = mJobDao.insertJob(jobEntry);
                mCallback.onJobInserted(jobId);
            }
        });
    }

    public void updateJob(final JobEntry jobEntry) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mJobDao.updateJob(jobEntry);
            }
        });
    }

    public void updateExpenses(final List<ExpenseEntry> expensesList) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mExpenseDao.updateExpenses(expensesList);
            }
        });
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface GetJobIdHandler {
        void onCategoryInserted(long categoryId);
        void onJobInserted(long expensesId);
    }

    public void setCallbackListener(GetJobIdHandler getIdCallback){
        mCallback = getIdCallback;
    }


    public LiveData<JobEntry> loadJobById(Long jobId) {
        return mJobDao.loadJobById(jobId);
    }


    public LiveData<List<ExpenseEntry>> loadExpensesByIds(long[] expensesId) {
        return mExpenseDao.loadExpensesById(expensesId);
    }

    public LiveData<List<CategoryEntry>> getCategories(CategoryEntry.Type type) {
        return mCategoryDao.loadCategories(type);
    }

    public void debugPrint() {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<JobEntry> jobEntries = mJobDao.debugLoadAllJobs();
                List<ExpenseEntry> expenseEntries = mExpenseDao.debugLoadAllExpenses();
                List<CategoryEntry> categoryEntries = mCategoryDao.debugLoadCategories();
                for(JobEntry jobEntry : jobEntries){
                    Log.i(TAG, jobEntry.toString());
                }
                for(ExpenseEntry expenseEntry : expenseEntries){
                    Log.i(TAG, expenseEntry.toString());
                }
                for(CategoryEntry categoryEntry : categoryEntries){
                    Log.i(TAG, categoryEntry.toString());
                }
            }
        });
    }

    public void insertCategory(final CategoryEntry categoryEntry) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                long categoryId = mCategoryDao.insertCategory(categoryEntry);
                mCallback.onCategoryInserted(categoryId);
            }
        });
    }
}
