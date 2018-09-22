package roiattia.com.capstone.ui.newjob;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.List;

import roiattia.com.capstone.database.dao.CategoryDao;
import roiattia.com.capstone.database.entry.CategoryEntry;
import roiattia.com.capstone.database.dao.ExpenseDao;
import roiattia.com.capstone.database.entry.ExpenseEntry;
import roiattia.com.capstone.database.dao.JobDao;
import roiattia.com.capstone.database.entry.JobEntry;
import roiattia.com.capstone.database.AppExecutors;

public class JobRepository {

    private static final String TAG = JobRepository.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static JobRepository sInstance;
    private final JobDao mJobDao;
    private final CategoryDao mCategoryDao;
    private final ExpenseDao mExpenseDao;
    private final AppExecutors mExecutors;
    private DataInsertHandler mCallback;

    private JobRepository(JobDao jobDao, CategoryDao categoryDao, ExpenseDao expenseDao,
                          AppExecutors appExecutors, DataInsertHandler callback){
        mJobDao = jobDao;
        mCategoryDao = categoryDao;
        mExpenseDao = expenseDao;
        mExecutors = appExecutors;
        mCallback = callback;
    }

    public synchronized static JobRepository getInstance(
            JobDao jobDao, CategoryDao categoryDao, ExpenseDao expenseDao,
            AppExecutors appExecutors, DataInsertHandler callback) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new JobRepository(jobDao, categoryDao, expenseDao, appExecutors, callback);
            }
        }
        return sInstance;
    }

    public LiveData<ExpenseEntry> loadExpenseByIdLiveData(long expenseId) {
        return mExpenseDao.loadExpenseByIdLiveData(expenseId);
    }


    /**
     * The interface that receives onJobClick messages.
     */
    public interface DataInsertHandler {
        void onCategoryInserted(long categoryId);
        void onJobInserted(long expensesId);
        void onExpenseLoaded(ExpenseEntry expenseEntry);
    }


//    public void loadExpenseById(final long expenseId) {
//        mExecutors.diskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                ExpenseEntry expenseEntry = mExpenseDao.loadExpenseById(expenseId);
//                mCallback.onExpenseLoaded(expenseEntry);
//            }
//        });
//    }

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

    public LiveData<JobEntry> loadJobById(Long jobId) {
        return mJobDao.loadJobById(jobId);
    }


    public LiveData<List<ExpenseEntry>> loadExpensesByIds(long[] expensesId) {
        return mExpenseDao.loadExpensesById(expensesId);
    }

    public LiveData<List<CategoryEntry>> getCategories(CategoryEntry.Type type) {
        return mCategoryDao.loadCategories(type);
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
}
