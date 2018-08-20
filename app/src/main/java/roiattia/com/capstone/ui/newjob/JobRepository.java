package roiattia.com.capstone.ui.newjob;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import org.joda.time.LocalDate;
import java.util.List;

import roiattia.com.capstone.database.CategoryDao;
import roiattia.com.capstone.database.CategoryEntry;
import roiattia.com.capstone.database.ExpenseDao;
import roiattia.com.capstone.database.ExpenseEntry;
import roiattia.com.capstone.database.JobDao;
import roiattia.com.capstone.database.JobEntry;
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
    private GetIdHandler mGetIdInBackground;

    private JobRepository(JobDao jobDao, CategoryDao categoryDao, ExpenseDao expenseDao,
                          AppExecutors appExecutors, GetIdHandler getIdHandler){
        mJobDao = jobDao;
        mCategoryDao = categoryDao;
        mExpenseDao = expenseDao;
        mExecutors = appExecutors;
        mGetIdInBackground = getIdHandler;
    }

    public synchronized static JobRepository getInstance(
            JobDao jobDao, CategoryDao categoryDao, ExpenseDao expenseDao,
            AppExecutors appExecutors, GetIdHandler getIdHandler) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new JobRepository(jobDao, categoryDao, expenseDao, appExecutors, getIdHandler);
            }
        }
        return sInstance;
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface GetIdHandler {
        void onCategoryInserted(long categoryId , CategoryEntry.Type type);
        void onJobInserted(long jobId);
    }

    // Jobs table methods
    public LiveData<List<JobEntry>> getJobs(){
        return mJobDao.loadAllJobs();
    }

    public LiveData<List<JobEntry>> getJobsByDate(final LocalDate localDate){
        return mJobDao.loadJobsAtDate(localDate);
    }

    public LiveData<List<CategoryEntry>> getJobsCategories(){
        return mCategoryDao.loadCategories(CategoryEntry.Type.JOB);
    }

    public void insertJob(final JobEntry jobEntry){
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                long jobId = mJobDao.insertJob(jobEntry);
                mGetIdInBackground.onJobInserted(jobId);
            }
        });
    }

    // Expanses table methods
    public void insertExpense(ExpenseEntry expenseEntry){
        mExpenseDao.insertExpense(expenseEntry);
    }

    // Categories table methods
    public LiveData<List<CategoryEntry>> getExpensesCategories(){
        return mCategoryDao.loadCategories(CategoryEntry.Type.EXPENSE);
    }

    public void insertCategory(final CategoryEntry categoryEntry, final CategoryEntry.Type type){
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                long id = mCategoryDao.insertCategory(categoryEntry);
                mGetIdInBackground.onCategoryInserted(id, type);
            }
        });
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
}
