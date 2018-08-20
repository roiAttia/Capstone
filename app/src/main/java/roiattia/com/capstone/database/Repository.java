package roiattia.com.capstone.database;

import android.arch.lifecycle.LiveData;

import org.joda.time.LocalDate;
import java.util.List;

import roiattia.com.capstone.utils.AppExecutors;

public class Repository {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static Repository sInstance;
    private final JobDao mJobDao;
    private final CategoryDao mCategoryDao;
    private final ExpenseDao mExpenseDao;
    private final AppExecutors mExecutors;
    private GetCategoryIdHandler mGetJobIdInBackground;

    private Repository(JobDao jobDao, CategoryDao categoryDao, ExpenseDao expenseDao,
                       AppExecutors appExecutors){
        mJobDao = jobDao;
        mCategoryDao = categoryDao;
        mExpenseDao = expenseDao;
        mExecutors = appExecutors;
    }

    public synchronized static Repository getInstance(
            JobDao jobDao, CategoryDao categoryDao, ExpenseDao expenseDao,
            AppExecutors appExecutors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new Repository(jobDao, categoryDao, expenseDao, appExecutors);
            }
        }
        return sInstance;
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface GetCategoryIdHandler {
        void onFinish(long jobId ,CategoryEntry.Type type);
    }

    public void setGetJobIdInBackground(GetCategoryIdHandler getJobIdInBackground){
        mGetJobIdInBackground = getJobIdInBackground;
    }

    public void insertJobInBackground(final JobEntry jobEntry){
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                long jobId = mJobDao.insertJob(jobEntry);
//                mGetJobIdInBackground.onFinish(jobId);
            }
        });
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
                mJobDao.insertJob(jobEntry);
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
                mGetJobIdInBackground.onFinish(id, type);
            }
        });
    }

    public void extractCategoryName(final int categoryId){
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                String categoryName = mCategoryDao.getCategoryName(categoryId);
            }
        });
    }

    public LiveData<List<CategoryEntry>> getCategories(CategoryEntry.Type type) {
        return mCategoryDao.loadCategories(type);
    }
}
